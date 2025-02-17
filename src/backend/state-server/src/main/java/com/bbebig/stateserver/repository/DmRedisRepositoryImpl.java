package com.bbebig.stateserver.repository;

import com.bbebig.commonmodule.redis.repository.DmRedisRepository;
import com.bbebig.commonmodule.redis.util.DmRedisKeys;
import com.bbebig.commonmodule.redis.util.DmRedisTTL;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DmRedisRepositoryImpl implements DmRedisRepository {

	// 숫자형 Set 전용
	private final RedisTemplate<String, Long> redisSetTemplate;

	// Spring Data Redis가 제공하는 인터페이스 (런타임에 초기화 필요)
	private SetOperations<String, Long> setOps;

	/**
	 * 스프링 빈 초기화 후(setup) opsForSet() 할당
	 */
	@PostConstruct
	public void init() {
		this.setOps = redisSetTemplate.opsForSet();
	}

	/**
	 * DM 채널에 참여하고 있는 멤버 목록을 Set 구조로 저장
	 * ex) dm:{channelId}:memberList => Set<MemberId>
	 * TTL: 1일
	 */
	@Override
	public void saveDmMemberSet(Long channelId, List<Long> memberIdList) {
		String key = DmRedisKeys.getDmMemberListKey(channelId);

		// Long[] 변환 후 Set 추가
		setOps.add(key, memberIdList.toArray(new Long[0]));

		// TTL 설정 (초 단위)
		redisSetTemplate.expire(key, DmRedisTTL.getPrivateDmMemberListTTLDate(), TimeUnit.SECONDS);
	}

	@Override
	public void addDmMemberToSet(Long channelId, Long memberId) {
		String key = DmRedisKeys.getDmMemberListKey(channelId);
		setOps.add(key, memberId);
		redisSetTemplate.expire(key, DmRedisTTL.getPrivateDmMemberListTTLDate(), TimeUnit.SECONDS);
	}

	@Override
	public void removeDmMemberFromSet(Long channelId, Long memberId) {
		String key = DmRedisKeys.getDmMemberListKey(channelId);
		setOps.remove(key, memberId);
	}

	@Override
	public boolean existsDmMemberList(Long channelId) {
		String key = DmRedisKeys.getDmMemberListKey(channelId);
		return Boolean.TRUE.equals(redisSetTemplate.hasKey(key));
	}

	@Override
	public Set<Long> getDmMemberList(Long channelId) {
		String key = DmRedisKeys.getDmMemberListKey(channelId);
		return setOps.members(key); // 이미 Long 타입 반환
	}

	@Override
	public void deleteDmMemberList(Long channelId) {
		String key = DmRedisKeys.getDmMemberListKey(channelId);
		redisSetTemplate.delete(key);
	}
}
