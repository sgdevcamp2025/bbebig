package com.bbebig.stateserver.repository;

import com.bbebig.commonmodule.redis.RedisKeys;
import com.bbebig.stateserver.domain.MemberPresenceStatus;
import com.bbebig.stateserver.domain.ServerMemberStatus;
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
public class RedisRepository {

	private final RedisTemplate<String, Object> redisTemplate;

	//서버별 멤버 목록(server:{serverId}:memberList)을 Set 자료구조로 관리
	public void saveServerMemberSet(Long serverId, List<Long> memberIdList) {
		String key = RedisKeys.getServerMemberListKey(serverId);
		redisTemplate.opsForSet().add(key, memberIdList.toArray());
	}


	// 서버별 멤버 목록에 멤버 추가
	public void addServerMemberToSet(Long serverId, Long memberId) {
		String key = RedisKeys.getServerMemberListKey(serverId);
		redisTemplate.opsForSet().add(key, memberId);
	}

	// 서버별 멤버 목록에서 멤버 삭제
	public void removeServerMemberFromSet(Long serverId, Long memberId) {
		String key = RedisKeys.getServerMemberListKey(serverId);
		redisTemplate.opsForSet().remove(key, memberId);
	}

	// 서버별 멤버 목록이 존재하는지 확인
	public boolean existsServerMemberList(Long serverId) {
		String key = RedisKeys.getServerMemberListKey(serverId);
		return Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}

	/**
	 * 서버별 멤버 목록 조회
	 * ex) SMEMBERS server:1001:memberList
	 */
	public Set<Long> getServerMemberList(Long serverId) {
		String key = RedisKeys.getServerMemberListKey(serverId);
		Set<Object> memberSet = redisTemplate.opsForSet().members(key);
		if (memberSet == null) {
			return Set.of();
		}
		return memberSet.stream()
				.map(this::convertToLong)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
	}

	/**
	 * 개별 유저의 presence 상태(key=state:{memberId}:memberStatus)를 ValueOperations로 저장
	 * ex) set("state:1:memberStatus", MemberPresenceStatus)
	 */
	public void saveMemberPresenceStatus(Long memberId, MemberPresenceStatus status) {
		String key = RedisKeys.getMemberStatusKey(memberId);
		redisTemplate.opsForValue().set(key, status);
		log.info("[RedisRepository] saveMemberPresenceStatus -> key={}, status={}", key, status);
	}

	/**
	 * 개별 유저의 presence 상태 조회
	 * ex) get("state:1:memberStatus") => MemberPresenceStatus
	 */
	public MemberPresenceStatus getMemberPresenceStatus(Long memberId) {
		String key = RedisKeys.getMemberStatusKey(memberId);
		Object obj = redisTemplate.opsForValue().get(key);
		if (obj instanceof MemberPresenceStatus) {
			return (MemberPresenceStatus) obj;
		}
		return null;
	}

	/**
	 * 서버별 멤버 상태(server:{serverId}:serverMemberStatus) -> Hash
	 * field=memberId, value=ServerMemberStatus
	 */
	public void saveServerMemberPresenceStatus(Long serverId, Long memberId, ServerMemberStatus status) {
		String key = RedisKeys.getServerMemberPresenceStatusKey(serverId);
		redisTemplate.opsForHash().put(key, String.valueOf(memberId), status);
		log.info("[RedisRepository] saveServerMemberPresenceStatus -> key={}, memberId={}, status={}", key, memberId, status);
	}

	// 서버별 멤버 상태 정보 삭제
	public void removeServerMemberPresenceStatus(Long serverId, Long memberId) {
		String key = RedisKeys.getServerMemberPresenceStatusKey(serverId);
		redisTemplate.opsForHash().delete(key, String.valueOf(memberId));
	}

	/**
	 * 서버별 멤버 상태 캐시 존재 여부
	 */
	public boolean existsServerMemberPresenceStatus(Long serverId) {
		String key = RedisKeys.getServerMemberPresenceStatusKey(serverId);
		return Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}

	/**
	 * 서버별 특정 멤버 상태 조회
	 */
	public ServerMemberStatus getServerMemberStatus(Long serverId, Long memberId) {
		String key = RedisKeys.getServerMemberPresenceStatusKey(serverId);
		Object obj = redisTemplate.opsForHash().get(key, String.valueOf(memberId));
		if (obj instanceof ServerMemberStatus) {
			return (ServerMemberStatus) obj;
		}
		return null;
	}

	/**
	 * 객체를 Long으로 변환(내부 유틸)
	 */
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
