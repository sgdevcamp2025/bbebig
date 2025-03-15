package com.bbebig.searchserver.domain.history.service;

import com.bbebig.commonmodule.clientDto.SearchFeignResponseDto.ServerChannelSequenceResponseDto;
import com.bbebig.commonmodule.clientDto.ServiceFeignResponseDto.*;
import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import com.bbebig.commonmodule.kafka.dto.ChatMessageDto;
import com.bbebig.commonmodule.kafka.dto.model.ChatType;
import com.bbebig.commonmodule.redis.domain.ChannelLastInfo;
import com.bbebig.commonmodule.redis.domain.ServerLastInfo;
import com.bbebig.searchserver.domain.history.repository.ChannelChatMessageRepository;
import com.bbebig.searchserver.domain.history.repository.DmChatMessageRepository;
import com.bbebig.searchserver.global.feign.client.ServiceClient;
import com.bbebig.searchserver.domain.history.domain.ChannelChatMessage;
import com.bbebig.searchserver.domain.history.domain.DmChatMessage;
import com.bbebig.searchserver.domain.history.dto.HistoryDtoConverter;
import com.bbebig.searchserver.domain.history.dto.HistoryResponseDto.ChannelUnreadCountDto;
import com.bbebig.searchserver.domain.history.dto.HistoryResponseDto.ServerUnreadCountDto;
import com.bbebig.searchserver.domain.search.repository.*;
import com.bbebig.searchserver.global.repository.MemberRedisRepositoryImpl;
import com.bbebig.searchserver.global.repository.ServerRedisRepositoryImpl;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.bbebig.searchserver.domain.history.dto.HistoryResponseDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class HistoryService {

	private final ChannelChatMessageRepository channelChatMessageRepository;
	private final DmChatMessageRepository dmChatMessageRepository;
	private final ChannelChatMessageElasticRepository channelChatMessageElasticRepository;
	private final DmChatMessageElasticRepository dmChatMessageElasticRepository;

	private final ServerRedisRepositoryImpl serverRedisRepository;
	private final MemberRedisRepositoryImpl memberRedisRepository;

	private final ServiceClient serviceClient;


	@Transactional
	public void saveChannelMessage(ChatMessageDto messageDto) {
		ChannelChatMessage message = HistoryDtoConverter.convertDtoToChannelChatMessage(messageDto);
		channelChatMessageRepository.save(message);
		channelChatMessageElasticRepository.save(HistoryDtoConverter.convertDtoToChannelChatMessageElastic(messageDto));

		if (ChatType.CHANNEL.equals(messageDto.getChatType())) {
			Long channelId = messageDto.getChannelId();
			if (!serverRedisRepository.existsChannelMessageList(channelId) ||
					serverRedisRepository.getChannelMessageList(channelId).isEmpty()) {
				cacheChannelMessage(channelId);
			} else {
				serverRedisRepository.saveChannelMessage(channelId, message);
			}
		}
	}


	public void saveDmMessage(ChatMessageDto messageDto) {
		DmChatMessage message = HistoryDtoConverter.convertDtoToDmChatMessage(messageDto);
		dmChatMessageRepository.save(message);
		dmChatMessageElasticRepository.save(HistoryDtoConverter.convertDtoToDmChatMessageElastic(messageDto));
	}

	public void updateChannelMessage(ChatMessageDto messageDto) {
		Optional<ChannelChatMessage> optionalMessage = channelChatMessageRepository.findById(messageDto.getId());

		optionalMessage.ifPresentOrElse(chatMessage -> {
			if (chatMessage.isDeleted()) {
				log.error("[Search] ChatMessageService : 삭제된 메시지는 수정할 수 없습니다. messageId: {}", messageDto.getId());
				throw new ErrorHandler(ErrorStatus.CANNOT_MODIFY_DELETED_MESSAGE);
			}

			chatMessage.updateContent(messageDto.getContent());
			channelChatMessageRepository.save(chatMessage);
			channelChatMessageElasticRepository.save(HistoryDtoConverter.convertDtoToChannelChatMessageElastic(messageDto));

			Long channelId = messageDto.getChannelId();
			// Redis에 채널 메시지가 존재하면 삭제 후 다시 저장
			List<ChannelChatMessage> channelMessageList = serverRedisRepository.getChannelMessageList(channelId);
			if (channelMessageList != null && !channelMessageList.isEmpty()) {
				channelMessageList.stream()
						.filter(m -> m.getId().equals(messageDto.getId()))
						.forEach(m -> {
							m.setContent(messageDto.getContent());
						});
				serverRedisRepository.saveChannelMessages(channelId, channelMessageList);
			} else {
				cacheChannelMessage(channelId);
			}


		}, () -> {
			log.error("[Search] ChatMessageService : 채팅 메시지 정보 없음. messageId: {}", messageDto.getId());
			throw new ErrorHandler(ErrorStatus.CHAT_MESSAGE_NOT_FOUND);
		});
	}

	public void updateDmMessage(ChatMessageDto messageDto) {
		Optional<DmChatMessage> optionalMessage = dmChatMessageRepository.findById(messageDto.getId());

		optionalMessage.ifPresentOrElse(chatMessage -> {
			if (chatMessage.isDeleted()) {
				log.error("[Search] ChatMessageService : 삭제된 메시지는 수정할 수 없습니다. messageId: {}", messageDto.getId());
				throw new ErrorHandler(ErrorStatus.CANNOT_MODIFY_DELETED_MESSAGE);
			}

			chatMessage.updateContent(messageDto.getContent());
			dmChatMessageRepository.save(chatMessage);
			dmChatMessageElasticRepository.save(HistoryDtoConverter.convertDtoToDmChatMessageElastic(messageDto));

		}, () -> {
			log.error("[Search] ChatMessageService : 채팅 메시지 정보 없음. messageId: {}", messageDto.getId());
			throw new ErrorHandler(ErrorStatus.CHAT_MESSAGE_NOT_FOUND);
		});
	}

	public void deleteChannelMessage(Long messageId) {
		Optional<ChannelChatMessage> optionalMessage = channelChatMessageRepository.findById(messageId);

		optionalMessage.ifPresentOrElse(chatMessage -> {
			if (chatMessage.isDeleted()) {
				log.warn("[Search] ChatMessageService : 이미 삭제된 메시지입니다. messageId: {}", messageId);
				throw new ErrorHandler(ErrorStatus.MESSAGE_ALREADY_DELETED);
			}

			chatMessage.delete();
			channelChatMessageRepository.save(chatMessage);
			channelChatMessageElasticRepository.deleteById(messageId); // Hard Delete in Elasticsearch

			Long channelId = chatMessage.getChannelId();
			// Redis에 채널 메시지가 존재하면 삭제 후 다시 저장
			if (serverRedisRepository.existsChannelMessageList(channelId)) {
				serverRedisRepository.deleteChannelMessageList(channelId);
			}

		}, () -> {
			log.error("[Search] ChatMessageService : 채널 채팅 메시지를 찾을 수 없습니다. messageId: {}", messageId);
			throw new ErrorHandler(ErrorStatus.CHAT_MESSAGE_NOT_FOUND);
		});
	}

	public void deleteDmMessage(Long messageId) {
		Optional<DmChatMessage> optionalMessage = dmChatMessageRepository.findById(messageId);

		optionalMessage.ifPresentOrElse(chatMessage -> {
			if (chatMessage.isDeleted()) {
				log.warn("[Search] ChatMessageService : 이미 삭제된 메시지입니다. messageId: {}", messageId);
				throw new ErrorHandler(ErrorStatus.MESSAGE_ALREADY_DELETED);
			}

			chatMessage.delete();
			dmChatMessageRepository.save(chatMessage);
			dmChatMessageElasticRepository.deleteById(messageId); // Hard Delete in Elasticsearch
			log.info("[Search] ChatMessageService : DM 메시지 삭제 완료. messageId: {}", messageId);
		}, () -> {
			log.error("[Search]ChatMessageService : DM 채팅 메시지를 찾을 수 없습니다. messageId: {}", messageId);
			throw new ErrorHandler(ErrorStatus.CHAT_MESSAGE_NOT_FOUND);
		});
	}

	// Id 기반 채널 과거 메시지 조회
	// GET /server/{serverId}/channel/{channelId}/messages
	public GetChannelMessageResponseDto getChannelMessages(Long serverId, Long channelId, Long sequence, int limit) {
		// 처음 검색이라면, 캐시된 최근 메시지를 반환
		if (sequence == null) {
			List<ChannelChatMessage> cachedChannelMessages = getCachedChannelMessages(channelId);
			return GetChannelMessageResponseDto.builder()
					.serverId(serverId)
					.channelId(channelId)
					.totalCount(cachedChannelMessages.size())
					.messages(cachedChannelMessages)
					.build();
		}

		PageRequest pageReq = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC));
		List<ChannelChatMessage> messages = channelChatMessageRepository.findPreviousMessages(channelId, sequence, pageReq);
		if (!messages.isEmpty()) {
			return HistoryDtoConverter.convertToGetChannelMessageResponseDto(serverId, channelId, messages);
		}
		else {
			return GetChannelMessageResponseDto.builder()
					.serverId(serverId)
					.channelId(channelId)
					.totalCount(0)
					.build();
		}
	}

	// ID 기반 DM 과거 메시지 조회
	// GET /dm/{channelId}/messages
	public GetDmMessageResponseDto getDmMessages(Long channelId, Long messageId, int limit) {
		if (messageId == null) {
			messageId = Long.MAX_VALUE;
		}

		PageRequest pageReq = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC));
		List<DmChatMessage> messages = dmChatMessageRepository.findByChannelIdAndIdLessThan(channelId, messageId, pageReq);
		if (!messages.isEmpty()) {
			return HistoryDtoConverter.convertToGetDmMessageResponseDto(channelId, messages);
		}
		else {
			return GetDmMessageResponseDto.builder()
					.channelId(channelId)
					.totalCount(0)
					.build();
		}
	}

	// 멤버가 참여한 서버별 안읽은 메시지 수 조회
	// GET /member/{memberId}/unread/server/all
	public AllServerUnreadCountDto getAllServerUnreadCount(Long memberId) {
		// TODO: 메시지 새로 들어왔을때 어떻게 처리할지 고민

		List<ServerUnreadCountDto> result = new ArrayList<>();
		Set<Long> serverList = memberRedisRepository.getMemberServerList(memberId);

		MemberServerListResponseDto memberServerListCacheResponseDto = serviceClient.getMemberServerList(memberId);
		serverList.addAll(memberServerListCacheResponseDto.getServerIdList());

		for (Long serverId : serverList) {
			result.add(getServerUnreadCount(memberId, serverId));
		}

		return AllServerUnreadCountDto.builder()
				.memberId(memberId)
				.totalServerCount(result.size())
				.serverInfos(result)
				.build();
	}

	public ServerUnreadCountDto getServerUnreadCount(Long memberId, Long serverId) {
		ServerLastInfo memberServerLastInfo = getServerLastInfo(memberId, serverId);

		if (memberServerLastInfo == null) {
			log.error("[Search] ChatMessageService: 서버 마지막 방문 정보 조회 실패. serverId: {}, memberId: {}", serverId, memberId);
			throw new ErrorHandler(ErrorStatus.SERVER_LAST_INFO_NOT_FOUND);
		}

		List<ChannelUnreadCountDto> channelUnreadList = new ArrayList<>();
		int serverTotalUnread = 0;

		for (Long channelId : memberServerLastInfo.getChannelLastInfoMap().keySet()) {
			ChannelLastInfo channelLastInfo = memberServerLastInfo.getChannelLastInfoMap().get(channelId);
			if (channelLastInfo == null) {
				log.error("[Search] ChatMessageService: 채널 마지막 방문 정보 조회 실패. serverId: {}, memberId: {}, channelId: {}", serverId, memberId, channelId);
				throw new ErrorHandler(ErrorStatus.CHANNEL_LAST_INFO_NOT_FOUND);
			}

			ServerChannelSequenceResponseDto channelLastSequence = getChannelLastSequence(channelId);

			int unread = (int)(channelLastSequence.getLastSequence() - channelLastInfo.getLastReadSequence());

			channelUnreadList.add(ChannelUnreadCountDto.builder()
					.channelId(channelId)
					.unreadCount(unread)
					.build());

			serverTotalUnread += unread;
		}

		return ServerUnreadCountDto.builder()
				.serverId(serverId)
				.channels(channelUnreadList)
				.totalUnread(serverTotalUnread)
				.build();
	}

	public List<ChannelChatMessage> getCachedChannelMessages(Long channelId) {
		if (!serverRedisRepository.existsChannelMessageList(channelId)) {
			cacheChannelMessage(channelId);
		}
		return serverRedisRepository.getChannelMessageList(channelId);
	}

	public ServerChannelSequenceResponseDto getChannelLastSequence(Long channelId) {
		Long lastSequence = serverRedisRepository.getServerChannelSequence(channelId);
		if (lastSequence == null) {
			Optional<ChannelChatMessage> topByChannelIdOrderByIdDesc = channelChatMessageRepository.findTopByChannelIdAndDeletedFalseOrderByIdDesc(channelId);
			if (topByChannelIdOrderByIdDesc.isPresent()) {
				lastSequence = topByChannelIdOrderByIdDesc.get().getSequence() == null ? 0L : topByChannelIdOrderByIdDesc.get().getSequence();
				serverRedisRepository.saveServerChannelSequence(channelId, lastSequence);
			} else {
				lastSequence = 0L;
				serverRedisRepository.saveServerChannelSequence(channelId, lastSequence);
			}
		}
		return ServerChannelSequenceResponseDto.builder()
				.channelId(channelId)
				.lastSequence(lastSequence)
				.build();
	}

	private void cacheChannelMessage(Long channelId) {
		List<ChannelChatMessage> messages = channelChatMessageRepository.findByChannelId(channelId,
				PageRequest.of(0, 100, Sort.by(Sort.Order.desc("sequence"))));
		if (messages != null && !messages.isEmpty()) {
			serverRedisRepository.saveChannelMessages(channelId, messages);
		}
	}

	private ServerLastInfo getServerLastInfo(Long memberId, Long serverId) {
		ServerLastInfo lastInfo = serverRedisRepository.getServerLastInfo(serverId, memberId);
		if (lastInfo == null) {
			ServerLastInfoResponseDto responseDto = serviceClient.getServerLastInfo(serverId, memberId);
			if (responseDto == null) {
				log.error("[Search] ChatMessageService: 서버 마지막 방문 정보 조회 실패. serverId: {}, memberId: {}", serverId, memberId);
				throw new ErrorHandler(ErrorStatus.SERVER_LAST_INFO_NOT_FOUND);
			}
			Map<Long, ChannelLastInfo> channelInfoMap = new HashMap<>();
			responseDto.getChannelInfoList().forEach(chDto -> {
				channelInfoMap.put(chDto.getChannelId(), ChannelLastInfo.builder()
						.channelId(chDto.getChannelId())
						.lastReadMessageId(chDto.getLastReadMessageId())
						.lastReadSequence(chDto.getLastReadSequence())
						.lastAccessAt(chDto.getLastAccessAt())
						.build());
			});
			ServerLastInfo info = ServerLastInfo.builder()
					.serverId(serverId)
					.channelLastInfoMap(channelInfoMap)
					.build();
			serverRedisRepository.saveServerLastInfo(memberId, serverId, info);
		}


		return lastInfo;
	}
}
