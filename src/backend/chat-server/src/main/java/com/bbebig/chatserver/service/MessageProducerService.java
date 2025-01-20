package com.bbebig.chatserver.service;

import com.bbebig.chatserver.dto.ChatMessageDto;
import com.bbebig.chatserver.dto.SessionEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageProducerService {

	@Value("${spring.kafka.topic.chat}")
	private String chatTopic;

	@Value("${spring.kafka.topic.session-event}")
	private String sessionEventTopic;

	private final KafkaTemplate<String, ChatMessageDto> kafkaTemplateForChat;

	private final KafkaTemplate<String, SessionEventDto> kafkaTemplateForSession;

	public void sendMessageForCommunity(ChatMessageDto messageDto) {
		kafkaTemplateForChat.send(chatTopic, messageDto);
	}

	public void sendMessageForSession(SessionEventDto sessionDto) {
		kafkaTemplateForSession.send(sessionEventTopic, sessionDto);
	}

}
