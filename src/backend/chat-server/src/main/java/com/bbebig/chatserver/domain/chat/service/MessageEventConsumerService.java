package com.bbebig.chatserver.domain.chat.service;

import com.bbebig.chatserver.domain.chat.repository.SessionManager;
import com.bbebig.commonmodule.kafka.dto.ChatMessageDto;
import com.bbebig.commonmodule.kafka.dto.model.ChannelType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageEventConsumerService {

	private final SimpMessageSendingOperations messagingTemplate;
	private final SessionManager sessionManager;

	@KafkaListener(topics = "${spring.kafka.topic.channel-chat-event}", groupId = "${spring.kafka.consumer.group-id.channel-chat-event}", containerFactory = "channelChatListener")
	public void consumeForChannelChatEvent(ChatMessageDto chatMessageDto) {
		if (chatMessageDto == null) {
			log.error("[Chat] MessageEventConsumerService: 채팅 메시지 정보 없음");
			return;
		}

		log.info("[Chat] MessageEventConsumerService: 채팅 메시지 수신. ChatMessageDto: {}", chatMessageDto);

		if (chatMessageDto.getChannelType() != ChannelType.CHANNEL) {
			log.error("[Chat] MessageEventConsumerService: 채널 채팅 메시지가 아닙니다. ChatMessageDto: {}", chatMessageDto);
			return;
		}
		messagingTemplate.convertAndSend("/topic/server/" + chatMessageDto.getServerId(), chatMessageDto);
	}

	@KafkaListener(topics = "${spring.kafka.topic.dm-chat-event}", groupId = "${spring.kafka.consumer.group-id.dm-chat-event}", containerFactory = "dmChatListener")
	public void consumeForDmChatEvent(ChatMessageDto chatMessageDto) {
		if (chatMessageDto == null) {
			log.error("[Chat] MessageEventConsumerService: 채팅 메시지 정보 없음");
			return;
		}

		if (chatMessageDto.getChannelType() != ChannelType.DM) {
			log.error("[Chat] MessageEventConsumerService: DM 채팅 메시지가 아닙니다. ChatMessageDto: {}", chatMessageDto);
			return;
		}

		// DM 채팅 대상자가 현재 서버에 접속 중인 경우에만 메시지 전송
		List<Long> memberIds = chatMessageDto.getTargetMemberIds();
		if (memberIds == null || memberIds.size() < 2) {
			log.error("[Chat] MessageEventConsumerService: DM 채팅 대상자 정보 없음. ChatMessageDto: {}", chatMessageDto);
			return;
		}

		for (Long memberId : memberIds) {
			if (sessionManager.isExistMemberId(memberId)) {
				messagingTemplate.convertAndSend("/queue/" + memberId, chatMessageDto);
			}
		}
	}

}
