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
public class KafkaProducerService {

	@Value("${spring.kafka.topic.channel-chat-event}")
	private String channelChatTopic;

	@Value("${spring.kafka.topic.dm-chat-event}")
	private String dmChatTopic;

	@Value("${spring.kafka.topic.connection-event}")
	private String sessionEventTopic;

	@Value("${spring.kafka.topic.channel-event}")
	private String channelEventTopic;

	private final KafkaTemplate<String, ChatMessageDto> kafkaTemplateForChannelMessageEvent;

	private final KafkaTemplate<String, ConnectionEventDto> kafkaTemplateForConnectionEvent;

	private final KafkaTemplate<String, ChannelEventDto> kafkaTemplateForChannelEvent;

	// 채널 채팅 메시지 전송
	public void sendMessageForChannelChat(ChatMessageDto messageDto) {
		kafkaTemplateForChannelMessageEvent.send(channelChatTopic, messageDto);
	}

	// DM 채팅 메시지 전송
	public void sendMessageForDmChat(ChatMessageDto messageDto) {
		kafkaTemplateForChannelMessageEvent.send(dmChatTopic, messageDto);
	}

	// 연결 이벤트 메시지 전송
	public void sendMessageForSession(ConnectionEventDto sessionDto) {
		kafkaTemplateForConnectionEvent.send(sessionEventTopic, sessionDto);
	}

	// 채널 이벤트 메시지 전송
	public void sendMessageForChannel(ChannelEventDto channelEventDto) {
		kafkaTemplateForChannelEvent.send(channelEventTopic, channelEventDto);
	}

}
