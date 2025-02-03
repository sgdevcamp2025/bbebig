package com.bbebig.stateserver.repository;

import com.bbebig.stateserver.domain.MemberPresenceStatus;
import com.bbebig.stateserver.domain.ServerMemberStatus;
import com.bbebig.stateserver.global.util.RedisKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisRepository {

	// TODO : RedisTemplate<String, Object>만 해도 되는지 이해하기
	private final RedisTemplate<String, Object> redisTemplate;

	// 서버별 참여중인 멤버 정보를 저장
	public void saveServerMemberList(Long serverId, List<Long> memberIdList) {
		String key = RedisKeys.getServerMemberListKey(serverId);
		redisTemplate.delete(key);
		redisTemplate.opsForList().rightPushAll(key, memberIdList);
	}

	// 서버별 참여중인 멤버 정보가 있는지 여부 반환
	public boolean existsServerMemberList(Long serverId) {
		String key = RedisKeys.getServerMemberListKey(serverId);
		return Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}

	// 서버별 참여중인 멤버 정보를 불러옴
	public List<Long> loadServerMemberList(Long serverId) {
		String key = RedisKeys.getServerMemberListKey(serverId);
		List<Object> objectList = redisTemplate.opsForList().range(key, 0, -1);

		if (objectList == null) {
			return List.of(); // null 반환 방지
		}

		return objectList.stream()
				.filter(Objects::nonNull) // null 값 필터링
				.map(obj -> {
					if (obj instanceof Long) {
						return (Long) obj; // 이미 Long이면 그대로 반환
					} else {
						return Long.valueOf(obj.toString()); // String이면 변환
					}
				})
				.collect(Collectors.toList());
	}
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

	// 서버 멤버 상태 정보를 저장 (HashMap)
	public void saveServerMemberPresenceStatus(Long serverId, Long memberId, ServerMemberStatus status) {
		String key = RedisKeys.getServerMemberPresenceStatusKey(serverId);
		redisTemplate.opsForHash().put(key, memberId, status);
	}

	// 서버 멤버 상태 정보 존재 여부 판단
	public boolean existsServerMemberPresenceStatus(Long serverId) {
		String key = RedisKeys.getServerMemberPresenceStatusKey(serverId);
		return Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}
}
