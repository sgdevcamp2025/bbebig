package com.bbebig.stateserver.domain.service;

import com.bbebig.commonmodule.kafka.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectionEventConsumerService {

	@KafkaListener(topics = "${spring.kafka.topic.connection-event}", groupId = "${spring.kafka.consumer.group-id.connection-event}", containerFactory = "connectionEventListener")
	public void consumeForChannelChatEvent(ChatMessageDto chatMessageDto) {

	}

}
