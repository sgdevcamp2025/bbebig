package com.bbebig.stateserver.service;

import com.bbebig.stateserver.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelEventConsumerService {

	private final RedisRepository redisRepository;

	@KafkaListener(topics = "${spring.kafka.topic.channel-event}", groupId = "${spring.kafka.consumer.group-id.channel-event}", containerFactory = "channelEventListener")
	public void consumeForChannelEvent(String channelEvent) {
		if (channelEvent == null) {
			log.error("[State] ChannelEventConsumerService: 채널 이벤트 정보 없음");
			return;
		}

	}
}
