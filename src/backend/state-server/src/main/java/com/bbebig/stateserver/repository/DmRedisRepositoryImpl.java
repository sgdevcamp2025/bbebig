package com.bbebig.stateserver.repository;

import com.bbebig.commonmodule.redis.repository.DmRedisRepository;
import com.bbebig.commonmodule.redis.util.RedisKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Repository
@RequiredArgsConstructor
public class DmRedisRepositoryImpl implements DmRedisRepository {

	private final RedisTemplate<String, Object> redisTemplate;

	/**
	 * DM 채널에 참여하고 있는 멤버 목록을 Set 구조로 저장
	 * ex) dm:{channelId}:memberList => Set<MemberId>
	 */
	@Override
	public void saveDmMemberSet(Long channelId, List<Long> memberIdList) {
		String key = RedisKeys.getDmMemberListKey(channelId);
		redisTemplate.opsForSet().add(key, memberIdList.toArray());
	}

	@Override
	public void addDmMemberToSet(Long channelId, Long memberId) {
		String key = RedisKeys.getDmMemberListKey(channelId);
		redisTemplate.opsForSet().add(key, memberId);
	}

	@Override
	public void removeDmMemberFromSet(Long channelId, Long memberId) {
		String key = RedisKeys.getDmMemberListKey(channelId);
		redisTemplate.opsForSet().remove(key, memberId);
	}

	@Override
	public boolean existsDmMemberList(Long channelId) {
		String key = RedisKeys.getDmMemberListKey(channelId);
		return Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}

	@Override
	public Set<Long> getDmMemberList(Long channelId) {
		String key = RedisKeys.getDmMemberListKey(channelId);
		Set<Object> memberSet = redisTemplate.opsForSet().members(key);
		if (memberSet == null) {
			return Set.of();
		}
		return memberSet.stream()
				.map(this::convertToLong)
				.filter(obj -> obj != null)
				.collect(Collectors.toSet());
	}

	// 객체를 Long으로 변환 (내부 메서드)
	public Long convertToLong(Object obj) {
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
