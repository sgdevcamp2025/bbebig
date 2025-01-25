package com.bbebig.chatserver.domain.service;

import com.bbebig.chatserver.domain.dto.serverEvent.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServerEventConsumerService {

	private final ObjectMapper objectMapper;

	private final SimpMessageSendingOperations messagingTemplate;

	@KafkaListener(topics = "${spring.kafka.topic.server-event}", groupId = "${spring.kafka.consumer.group-id.server-event}", containerFactory = "serverEventListener")
	public void consumeForServerEvent(ServerEventDto serverEventDto) {
		if (serverEventDto == null) {
			log.error("[Chat] ServerEventConsumerService: 서버 이벤트 정보 없음");
			return;
		}

		if (serverEventDto instanceof ServerChannelEventDto
				|| serverEventDto instanceof ServerCategoryEventDto
				|| serverEventDto instanceof ServerMemberPresenceEventDto
				|| serverEventDto instanceof ServerMemberActionEventDto) {
			log.error("[Chat] ServerEventConsumerService: 알려지지 않은 서버 이벤트 타입 수신. ServerEventDto: {}", serverEventDto);
			return;
		}
		messagingTemplate.convertAndSend("/topic/server/" + serverEventDto.getServerId(), serverEventDto);

	}
}
