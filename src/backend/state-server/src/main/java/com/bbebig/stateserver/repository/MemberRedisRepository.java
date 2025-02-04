package com.bbebig.stateserver.repository;

import com.bbebig.commonmodule.redis.RedisKeys;
import com.bbebig.commonmodule.redis.domain.MemberPresenceStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRedisRepository {

	private final RedisTemplate<String, Object> redisTemplate;

	/**
	 * 개별 유저의 참여중인 서버 목록을 Hash 구조로 저장
	 * ex) member:{memberId}:serverList => Set<ServerId>
	 */
	public void saveMemberServerSet(Long memberId, List<Long> serverIdList) {
		String key = RedisKeys.getMemberServerListKey(memberId);
		redisTemplate.opsForSet().add(key, serverIdList.toArray());
	}

	// 개별 유저의 참여중인 서버 목록에 서버 추가
	public void addMemberServerToSet(Long memberId, Long serverId) {
		String key = RedisKeys.getMemberServerListKey(memberId);
		redisTemplate.opsForSet().add(key, serverId);
	}

	// 개별 유저의 참여중인 서버 목록에서 서버 삭제
	public void removeMemberServerFromSet(Long memberId, Long serverId) {
		String key = RedisKeys.getMemberServerListKey(memberId);
		redisTemplate.opsForSet().remove(key, serverId);
	}

	// 개별 유저의 참여중인 서버 목록이 존재하는지 확인
	public boolean existsMemberServerList(Long memberId) {
		String key = RedisKeys.getMemberServerListKey(memberId);
		return Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}

	// 개별 유저의 참여중인 서버 목록 반환
	public Set<Long> getMemberServerList(Long memberId) {
		String key = RedisKeys.getMemberServerListKey(memberId);
		Set<Object> serverSet = redisTemplate.opsForSet().members(key);
		if (serverSet == null) {
			return Set.of();
		}
		return serverSet.stream()
				.map(this::convertToLong)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
	}

	/**
	 * 개별 유저의 presence 상태 및 Device Info를 Json을 통째로 저장
	 * ex) member:{memberId}:memberStatus => MemberPresenceStatus
	 */
	public void saveMemberPresenceStatus(Long memberId, MemberPresenceStatus status) {
		String key = RedisKeys.getMemberStatusKey(memberId);
		redisTemplate.opsForValue().set(key, status);
		log.info("[RedisRepository] saveMemberPresenceStatus -> key={}, status={}", key, status);
	}

	// 개별 유저 presence 상태 정보 조회
	public MemberPresenceStatus getMemberPresenceStatus(Long memberId) {
		String key = RedisKeys.getMemberStatusKey(memberId);
		Object obj = redisTemplate.opsForValue().get(key);
		if (obj instanceof MemberPresenceStatus) {
			return (MemberPresenceStatus) obj;
		}
		return null;
	}



	// 객체를 Long으로 변환 (내부 메서드)
	private Long convertToLong(Object obj) {
		try {
			if (obj instanceof Long) {
				return (Long) obj;
			}
			return Long.valueOf(obj.toString());
		} catch (NumberFormatException e) {
			log.error("convertToLong: 변환 실패, obj={}", obj, e);
			return null;
		}
	}
}
