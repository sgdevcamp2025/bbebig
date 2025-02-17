package com.bbebig.chatserver.domain.chat.controller;

import com.bbebig.chatserver.domain.chat.dto.TypingEventDto;
import com.bbebig.chatserver.domain.chat.service.TypingEventRedisPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class TypingEventController {

	private final TypingEventRedisPublisher redisPublisher;

	@MessageMapping("/channel/typing")
	public void sendTypingEvent(@Payload TypingEventDto typingEventDto) {
		// Redis Pub/Sub 채널로 이벤트 발행
		redisPublisher.publishTypingEvent(typingEventDto);
	}
}
