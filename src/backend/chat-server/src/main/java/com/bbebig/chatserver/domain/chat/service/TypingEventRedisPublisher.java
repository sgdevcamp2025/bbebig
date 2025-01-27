package com.bbebig.chatserver.domain.chat.service;

import com.bbebig.chatserver.domain.chat.dto.TypingEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TypingEventRedisPublisher {

	private final RedisTemplate<String, Object> redisTemplate;
	private final ChannelTopic typingChannelTopic;

	/**
	 * Redis Pub/Sub 채널에 메시지 발행
	 */
	public void publishTypingEvent(TypingEventDto typingEventDto) {
		try {
			redisTemplate.convertAndSend(typingChannelTopic.getTopic(), typingEventDto);
			log.info("[Chat] TypingEventRedisPublisher: 타이핑 이벤트 발행. senderId = {}, channelId = {}, serverId = {}", typingEventDto.getMemberId(), typingEventDto.getChannelId(), typingEventDto.getServerId());
		} catch (Exception e) {
			log.error("[Chat] TypingEventRedisPublisher: 타이핑 이벤트 파싱 실패. message = {}", e.getMessage());
		}
	}
}
