package com.bbebig.chatserver.domain.chat.service;

import com.bbebig.commonmodule.kafka.dto.ChannelEventDto;
import com.bbebig.commonmodule.kafka.dto.ChatMessageDto;
import com.bbebig.commonmodule.kafka.dto.ConnectionEventDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MessageProducerService {

	@Value("${spring.kafka.topic.channel-chat-event}")
	private String channelChatTopic;

	@Value("${spring.kafka.topic.dm-chat-event}")
	private String dmChatTopic;

	@Value("${spring.kafka.topic.connection-event}")
	private String sessionEventTopic;

	@Value("${spring.kafka.topic.channel-event}")
	private String channelEventTopic;

	private final KafkaTemplate<String, ChatMessageDto> kafkaTemplateForChannelChat;

	private final KafkaTemplate<String, ChatMessageDto> kafkaTemplateForDmChat;

	private final KafkaTemplate<String, ConnectionEventDto> kafkaTemplateForConnection;

	private final KafkaTemplate<String, ChannelEventDto> kafkaTemplateForChannel;

	// 채널 채팅 메시지 전송
	public void sendMessageForChannelChat(ChatMessageDto messageDto) {
		kafkaTemplateForChannelChat.send(channelChatTopic, messageDto);
	}

	// DM 채팅 메시지 전송
	public void sendMessageForDmChat(ChatMessageDto messageDto) {
		kafkaTemplateForDmChat.send(dmChatTopic, messageDto);
	}

	// 연결 이벤트 메시지 전송
	public void sendMessageForSession(ConnectionEventDto sessionDto) {
		kafkaTemplateForConnection.send(sessionEventTopic, sessionDto);
	}

	// 채널 이벤트 메시지 전송
	public void sendMessageForChannel(ChannelEventDto channelEventDto) {
		kafkaTemplateForChannel.send(channelEventTopic, channelEventDto);
	}

}
