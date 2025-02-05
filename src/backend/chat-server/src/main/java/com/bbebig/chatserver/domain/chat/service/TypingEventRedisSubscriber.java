package com.bbebig.chatserver.domain.chat.service;

import com.bbebig.chatserver.domain.chat.dto.TypingEventDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TypingEventRedisSubscriber {

	private final SimpMessagingTemplate messagingTemplate;
	private final ObjectMapper objectMapper;

	// Redis 채널에서 메시지를 받으면 WebSocket으로 전달
	public void onMessage(String message) {
		try {
			// JSON 문자열을 TypingEventDto로 변환
			TypingEventDto dto = objectMapper.readValue(message, TypingEventDto.class);
			// WebSocket으로 메시지 전송
			messagingTemplate.convertAndSend("/topic/channel/" + dto.getChannelId(), dto);
		} catch (Exception e) {
			log.error("[Chat] TypingEventRedisSubscriber: 타이핑 이벤트 파싱 실패. message = {}", e.getMessage());
		}
	}
}
