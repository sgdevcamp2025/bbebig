package com.bbebig.chatserver.service;

import com.bbebig.chatserver.dto.ChatMessageDto;
import com.bbebig.chatserver.dto.SessionEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageProducerService {

	@Value("${spring.kafka.topic.channel-chat}")
	private String channelChatTopic;

	@Value("${spring.kafka.topic.dm-chat}")
	private String dmChatTopic;

	@Value("${spring.kafka.topic.session-event}")
	private String sessionEventTopic;

	private final KafkaTemplate<String, ChatMessageDto> kafkaTemplateForChat;

	private final KafkaTemplate<String, SessionEventDto> kafkaTemplateForSession;

	// 채널 채팅 메시지 전송
	public void sendMessageForChannelChat(ChatMessageDto messageDto) {
		kafkaTemplateForChat.send(channelChatTopic, messageDto);
	}

	// DM 채팅 메시지 전송
	public void sendMessageForDmChat(ChatMessageDto messageDto) {
		kafkaTemplateForChat.send(channelChatTopic, messageDto);
	}

	public void sendMessageForSession(SessionEventDto sessionDto) {
		kafkaTemplateForSession.send(sessionEventTopic, sessionDto);
	}

}
