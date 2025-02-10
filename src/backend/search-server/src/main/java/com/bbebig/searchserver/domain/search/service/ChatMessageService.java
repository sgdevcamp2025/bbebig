package com.bbebig.searchserver.domain.search.service;

import com.bbebig.commonmodule.kafka.dto.ChatMessageDto;
import com.bbebig.commonmodule.kafka.dto.model.ChannelType;
import com.bbebig.searchserver.domain.search.client.ServiceClient;
import com.bbebig.searchserver.domain.search.domain.ChannelChatMessage;
import com.bbebig.searchserver.domain.search.domain.DmChatMessage;
import com.bbebig.searchserver.domain.search.dto.ChatMessageDtoConverter;
import com.bbebig.searchserver.domain.search.dto.ChatMessageResponseDto;
import com.bbebig.searchserver.domain.search.dto.ChatMessageResponseDto.ChannelUnreadCountDto;
import com.bbebig.searchserver.domain.search.dto.ChatMessageResponseDto.ServerUnreadCountDto;
import com.bbebig.searchserver.domain.search.repository.*;
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

import static com.bbebig.commonmodule.clientDto.serviceServer.CommonServiceServerClientResponseDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ChatMessageService {

	private final ChannelChatMessageRepository channelChatMessageRepository;
	private final DmChatMessageRepository dmChatMessageRepository;
	private final ChannelChatMessageElasticRepository channelChatMessageElasticRepository;
	private final DmChatMessageElasticRepository dmChatMessageElasticRepository;

	private final ServerRedisRepositoryImpl serverRedisRepository;
	private final MemberRedisRepositoryImpl memberRedisRepository;
	private final DmRedisRepositoryImpl dmRedisRepository;

	private final ServiceClient serviceClient;


	public void saveChannelMessage(ChatMessageDto messageDto) {
		ChannelChatMessage message = ChatMessageDtoConverter.convertDtoToChannelChatMessage(messageDto);
		channelChatMessageRepository.save(message);
		channelChatMessageElasticRepository.save(ChatMessageDtoConverter.convertDtoToChannelChatMessageElastic(messageDto));

		if (ChannelType.CHANNEL.equals(messageDto.getChannelType())) {
			Long channelId = messageDto.getChannelId();
			if (serverRedisRepository.existsChannelMessageList(channelId)) {
				cacheChannelMessage(channelId);
			}
			serverRedisRepository.saveChannelMessage(channelId, message);
		}
	}


	public void saveDmMessage(ChatMessageDto messageDto) {
		DmChatMessage message = ChatMessageDtoConverter.convertDtoToDmChatMessage(messageDto);
		dmChatMessageRepository.save(message);
		dmChatMessageElasticRepository.save(ChatMessageDtoConverter.convertDtoToDmChatMessageElastic(messageDto));
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
			channelChatMessageElasticRepository.save(ChatMessageDtoConverter.convertDtoToChannelChatMessageElastic(messageDto));

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
			dmChatMessageElasticRepository.save(ChatMessageDtoConverter.convertDtoToDmChatMessageElastic(messageDto));

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

	// 멤버가 참여한 서버별 안읽은 메시지 수 조회
	// GET /message/member/{memberId}/server/unread
	public List<ServerUnreadCountDto> getServerUnreadMessageCount(Long memberId) {
		List<ServerLastInfoResponseDto> serverLastInfo = getServerLastInfo(memberId);

		// TODO: 캐싱 할지 여부 판단해서 관리
		// TODO: 메시지 새로 들어왔을때 어떻게 처리할지 고민

		List<ServerUnreadCountDto> result = new ArrayList<>();
		for (ServerLastInfoResponseDto serverDto : serverLastInfo) {
			Long serverId = serverDto.getServerId();
			List<ChannelUnreadCountDto> channelUnreadList = new ArrayList<>();
			int serverTotalUnread = 0;

			for (ChannelLastInfoResponseDto chDto : serverDto.getChannelInfoList()) {
				long lastReadId = (chDto.getLastReadMessageId() == null) ? 0L : chDto.getLastReadMessageId();

				List<ChannelChatMessage> cachedMessages = serverRedisRepository.getChannelMessages(chDto.getChannelId());

				int unread = (int) cachedMessages.stream()
						.filter(msg -> msg.getId() != null && msg.getId() > lastReadId)
						.count();

				channelUnreadList.add(ChannelUnreadCountDto.builder()
						.channelId(chDto.getChannelId())
						.unreadCount(unread)
						.build());

				serverTotalUnread += unread;
			}

			result.add(ServerUnreadCountDto.builder()
					.serverId(serverId)
					.channels(channelUnreadList)
					.totalUnread(serverTotalUnread)
					.build());
		}
		return result;
	}

	public void cacheChannelMessage(Long channelId) {
		List<ChannelChatMessage> messages = channelChatMessageRepository.findByChannelId(channelId,
				PageRequest.of(0, 300, Sort.by(Sort.Order.desc("id"))));
		serverRedisRepository.saveChannelMessages(channelId, messages);
	}

	private List<ServerLastInfoResponseDto> getServerLastInfo(Long memberId) {
		Set<Long> memberServerList = memberRedisRepository.getMemberServerList(memberId);
		List<ServerLastInfoResponseDto> serverInfoList = new ArrayList<>();
		if (memberServerList.isEmpty()) {
			try {
				MemberServerListResponseDto memberServerListCacheResponseDto = serviceClient.getMemberServerList(memberId);
				memberServerList.addAll(memberServerListCacheResponseDto.getServerIdList());
			} catch (FeignException e) {
				// TODO: 예외 처리 로직 구현
				if (e.status() == 400) {
					// 400 처리
				}
				log.error("[Search] ChatMessageService: 멤버 서버 목록 조회 중 오류 발생. memberId: {}", memberId);
			}
		}
		for (Long serverId : memberServerList) {
			try {
				ServerLastInfoResponseDto serverLastInfoResponseDto = serviceClient.getServerLastInfo(serverId, memberId);
				serverInfoList.add(serverLastInfoResponseDto);
			} catch (FeignException e) {
				// TODO: 예외 처리 로직 구현
				if (e.status() == 400) {
					// 400 처리
				}
				log.error("[Search] ChatMessageService: 서버 정보 조회 중 오류 발생. serverId: {}, memberId: {}", serverId, memberId);
			}
		}
		return serverInfoList;
	}
}
