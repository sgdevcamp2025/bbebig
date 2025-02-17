package com.bbebig.searchserver.domain.history.service;

import com.bbebig.commonmodule.clientDto.ServiceFeignResponseDto.*;
import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import com.bbebig.commonmodule.kafka.dto.ChatMessageDto;
import com.bbebig.commonmodule.kafka.dto.model.ChannelType;
import com.bbebig.searchserver.domain.history.repository.ChannelChatMessageRepository;
import com.bbebig.searchserver.domain.history.repository.DmChatMessageRepository;
import com.bbebig.searchserver.global.client.ServiceClient;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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


	public void saveChannelMessage(ChatMessageDto messageDto) {
		ChannelChatMessage message = HistoryDtoConverter.convertDtoToChannelChatMessage(messageDto);
		channelChatMessageRepository.save(message);
		channelChatMessageElasticRepository.save(HistoryDtoConverter.convertDtoToChannelChatMessageElastic(messageDto));

		if (ChannelType.CHANNEL.equals(messageDto.getChannelType())) {
			Long channelId = messageDto.getChannelId();
			if (serverRedisRepository.existsChannelMessageList(channelId)) {
				cacheChannelMessage(channelId);
			}
			serverRedisRepository.saveChannelMessage(channelId, message);
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
				throw new IllegalStateException("삭제된 메시지는 수정할 수 없습니다.");
			}

			chatMessage.updateContent(messageDto.getContent());
			channelChatMessageRepository.save(chatMessage);
			channelChatMessageElasticRepository.save(HistoryDtoConverter.convertDtoToChannelChatMessageElastic(messageDto));

			Long channelId = messageDto.getChannelId();
			// Redis에 채널 메시지가 존재하면 삭제 후 다시 저장
			if (serverRedisRepository.existsChannelMessageList(channelId)) {
				serverRedisRepository.deleteChannelMessageList(chatMessage.getChannelId());
			}
			cacheChannelMessage(channelId);

		}, () -> {
			log.error("[Search] ChatMessageService : 채팅 메시지 정보 없음. messageId: {}", messageDto.getId());
			throw new IllegalArgumentException("채팅 메시지 정보 없음.");
		});
	}

	public void updateDmMessage(ChatMessageDto messageDto) {
		Optional<DmChatMessage> optionalMessage = dmChatMessageRepository.findById(messageDto.getId());

		optionalMessage.ifPresentOrElse(chatMessage -> {
			if (chatMessage.isDeleted()) {
				log.error("[Search] ChatMessageService : 삭제된 메시지는 수정할 수 없습니다. messageId: {}", messageDto.getId());
				throw new IllegalStateException("삭제된 메시지는 수정할 수 없습니다.");
			}

			chatMessage.updateContent(messageDto.getContent());
			dmChatMessageRepository.save(chatMessage);
			dmChatMessageElasticRepository.save(HistoryDtoConverter.convertDtoToDmChatMessageElastic(messageDto));

		}, () -> {
			log.error("[Search] ChatMessageService : 채팅 메시지 정보 없음. messageId: {}", messageDto.getId());
			throw new IllegalArgumentException("채팅 메시지 정보 없음.");
		});
	}

	public void deleteChannelMessage(Long messageId) {
		Optional<ChannelChatMessage> optionalMessage = channelChatMessageRepository.findById(messageId);

		optionalMessage.ifPresentOrElse(chatMessage -> {
			if (chatMessage.isDeleted()) {
				log.warn("[Search] ChatMessageService : 이미 삭제된 메시지입니다. messageId: {}", messageId);
				return;
			}

			chatMessage.delete();
			channelChatMessageRepository.save(chatMessage);
			channelChatMessageElasticRepository.deleteById(messageId); // Hard Delete in Elasticsearch

			Long channelId = chatMessage.getChannelId();
			// Redis에 채널 메시지가 존재하면 삭제 후 다시 저장
			if (serverRedisRepository.existsChannelMessageList(channelId)) {
				serverRedisRepository.deleteChannelMessageList(channelId);
			}

			log.info("[Search] ChatMessageService : 채널 메시지 삭제 완료. messageId: {}", messageId);
		}, () -> {
			log.error("[Search] ChatMessageService : 채널 채팅 메시지를 찾을 수 없습니다. messageId: {}", messageId);
			throw new IllegalArgumentException("채널 채팅 메시지 정보 없음.");
		});
	}

	public void deleteDmMessage(Long messageId) {
		Optional<DmChatMessage> optionalMessage = dmChatMessageRepository.findById(messageId);

		optionalMessage.ifPresentOrElse(chatMessage -> {
			if (chatMessage.isDeleted()) {
				log.warn("[Search] ChatMessageService : 이미 삭제된 메시지입니다. messageId: {}", messageId);
				return;
			}

			chatMessage.delete();
			dmChatMessageRepository.save(chatMessage);
			dmChatMessageElasticRepository.deleteById(messageId); // Hard Delete in Elasticsearch
			log.info("[Search] ChatMessageService : DM 메시지 삭제 완료. messageId: {}", messageId);
		}, () -> {
			log.error("[Search]ChatMessageService : DM 채팅 메시지를 찾을 수 없습니다. messageId: {}", messageId);
			throw new IllegalArgumentException("DM 채팅 메시지 정보 없음.");
		});
	}

	// Id 기반 채널 과거 메시지 조회
	// GET /server/{serverId}/channel/{channelId}/messages
	public GetChannelMessageResponseDto getChannelMessages(Long serverId, Long channelId, Long messageId, int limit) {
		// 처음 검색이라면, 캐시된 최근 메시지를 반환
		if (messageId == null) {
			List<ChannelChatMessage> cachedChannelMessages = getCachedChannelMessages(channelId);
			return GetChannelMessageResponseDto.builder()
					.serverId(serverId)
					.channelId(channelId)
					.totalCount(cachedChannelMessages.size())
					.messages(cachedChannelMessages)
					.build();
		}

		PageRequest pageReq = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC));
		List<ChannelChatMessage> messages = channelChatMessageRepository.findByChannelIdAndIdLessThan(channelId, messageId, pageReq);
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

		// TODO: 캐싱 할지 여부 판단해서 관리
		// TODO: 메시지 새로 들어왔을때 어떻게 처리할지 고민

		List<ServerUnreadCountDto> result = new ArrayList<>();
		Set<Long> serverList = memberRedisRepository.getMemberServerList(memberId);
		if (serverList.isEmpty()) {
			try {
				MemberServerListResponseDto memberServerListCacheResponseDto = serviceClient.getMemberServerList(memberId);
				serverList.addAll(memberServerListCacheResponseDto.getServerIdList());
			} catch (FeignException e) {
				log.error("[Search] ChatMessageService: 멤버 서버 목록 조회 중 오류 발생. memberId: {}", memberId);
			}
		}
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
		ServerLastInfoResponseDto serverLastInfo = getServerLastInfo(memberId, serverId);

		if (serverLastInfo == null) {
			log.error("[Search] ChatMessageService: 서버 마지막 방문 정보 조회 실패. serverId: {}, memberId: {}", serverId, memberId);
			throw new ErrorHandler(ErrorStatus.SERVER_LAST_INFO_NOT_FOUND);
		}

		List<ChannelUnreadCountDto> channelUnreadList = new ArrayList<>();
		int serverTotalUnread = 0;

		for (ChannelLastInfoResponseDto chDto : serverLastInfo.getChannelInfoList()) {
			long lastReadId = (chDto.getLastReadMessageId() == null) ? 0L : chDto.getLastReadMessageId();

			List<ChannelChatMessage> cachedMessages = getCachedChannelMessages(chDto.getChannelId());

			int unread = (int) cachedMessages.stream()
					.filter(msg -> msg.getId() != null && msg.getId() > lastReadId)
					.count();

			channelUnreadList.add(ChannelUnreadCountDto.builder()
					.channelId(chDto.getChannelId())
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
		return serverRedisRepository.getChannelMessages(channelId);
	}

	private void cacheChannelMessage(Long channelId) {
		List<ChannelChatMessage> messages = channelChatMessageRepository.findByChannelId(channelId,
				PageRequest.of(0, 300, Sort.by(Sort.Order.desc("id"))));
		serverRedisRepository.saveChannelMessages(channelId, messages);
	}

	private ServerLastInfoResponseDto getServerLastInfo(Long memberId, Long serverId) {
		ServerLastInfoResponseDto responseDto = null;
		try {
			responseDto = serviceClient.getServerLastInfo(serverId, memberId);
		} catch (FeignException e) {
			log.error("[Search] ChatMessageService: 서버 정보 조회 중 오류 발생. serverId: {}, memberId: {}", serverId, memberId);
		}
		return responseDto;
	}
}
