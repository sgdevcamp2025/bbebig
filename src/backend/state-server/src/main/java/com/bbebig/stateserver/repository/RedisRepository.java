package com.bbebig.stateserver.repository;

import com.bbebig.stateserver.domain.MemberPresenceStatus;
import com.bbebig.stateserver.global.util.RedisKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisRepository {

	// TODO : RedisTemplate<String, Object>만 해도 되는지 이해하기
	private final RedisTemplate<String, Object> redisTemplate;

	// 멤버 상태 정보를 저장
	public void saveMemberPresenceStatus(String key, MemberPresenceStatus status) {
		redisTemplate.opsForValue().set(key, status);
	}

	// 멤버 상태 정보를 불러옴
	public MemberPresenceStatus loadMemberPresenceStatus(String key) {
		Object obj = redisTemplate.opsForValue().get(key);
		if (obj instanceof MemberPresenceStatus) {
			return (MemberPresenceStatus) obj;
		}
		return null;
	}

	// 멤버 상태 정보 조회
	public MemberPresenceStatus getMemberPresenceStatus(Long memberId) {
		String key = RedisKeys.getMemberStatusKey(memberId);
		return loadMemberPresenceStatus(key);
	}

}
