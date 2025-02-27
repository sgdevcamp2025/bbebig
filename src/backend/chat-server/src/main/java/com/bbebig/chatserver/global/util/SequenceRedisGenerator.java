package com.bbebig.chatserver.global.util;

import com.bbebig.commonmodule.redis.util.ServerRedisKeys;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * Redis를 이용해 채널/서버 등의 시퀀스를 원자적으로 증가시키는 유틸 클래스
 */
@Component
@RequiredArgsConstructor
public class SequenceRedisGenerator {

	private final RedisTemplate<String, Long> redisSetTemplate;

	/**
	 * 채널 시퀀스를 1 증가시키고 결과값을 반환
	 * 예) key: "channel:{channelId}:seq"
	 */
	public Long nextSeqForServerChannel(Long channelId) {
		String redisKey = ServerRedisKeys.getServerChannelSequenceKey(channelId);
		return redisSetTemplate.opsForValue().increment(redisKey);
	}

	public Long getSeqForServerChannel(Long channelId) {
		String redisKey = ServerRedisKeys.getServerChannelSequenceKey(channelId);
		return redisSetTemplate.opsForValue().get(redisKey);
	}

}

