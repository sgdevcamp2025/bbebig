package com.bbebig.searchserver.domain.search.repository;

import com.bbebig.commonmodule.redis.domain.ServerMemberStatus;
import com.bbebig.commonmodule.redis.repository.ServerRedisRepository;
import com.bbebig.commonmodule.redis.util.ServerRedisKeys;
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
public class ServerRedisRepositoryImpl implements ServerRedisRepository {

	private final RedisTemplate<String, Object> redisTemplate;

	// TODO : ConvertToLong 이 잘 작동하는지, 문제 생긴다면 이 부분일 가능성이 높으니 체크하기

	/**
	 * 서버에 참여하고 있는 멤버 목록을 Hash 구조로 저장
	 * ex) server:{serverId}:serverMemberList => Set<MemberId>
	 */
	@Override
	public void saveServerMemberSet(Long serverId, List<Long> memberIdList) {
		String key = ServerRedisKeys.getServerMemberListKey(serverId);
		redisTemplate.opsForSet().add(key, memberIdList.toArray());
	}


	// 서버에 참여중인 멤버 목록에 멤버 추가
	@Override
	public void addServerMemberToSet(Long serverId, Long memberId) {
		String key = ServerRedisKeys.getServerMemberListKey(serverId);
		redisTemplate.opsForSet().add(key, memberId);
	}

	// 서버에 참여중인 멤버 목록에서 멤버 삭제
	@Override
	public void removeServerMemberFromSet(Long serverId, Long memberId) {
		String key = ServerRedisKeys.getServerMemberListKey(serverId);
		redisTemplate.opsForSet().remove(key, memberId);
	}

	// 서버별 멤버 목록이 존재하는지 확인
	@Override
	public boolean existsServerMemberList(Long serverId) {
		String key = ServerRedisKeys.getServerMemberListKey(serverId);
		return Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}

	// 서버에 참여중인 멤버 목록 반환
	@Override
	public Set<Long> getServerMemberList(Long serverId) {
		String key = ServerRedisKeys.getServerMemberListKey(serverId);
		Set<Object> memberSet = redisTemplate.opsForSet().members(key);
		if (memberSet == null) {
			return Set.of();
		}
		return memberSet.stream()
				.map(this::convertToLong)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
	}

	// 서버 삭제에 따른 서버별 멤버 목록 삭제
	@Override
	public void deleteServerMemberList(Long serverId) {
		String key = ServerRedisKeys.getServerMemberListKey(serverId);
		redisTemplate.delete(key);
	}

	/**
	 * 서버별 멤버 상태 정보를 Hash 구조로 저장
	 * (server:{serverId}:serverMemberStatus) => ServerMemberStatus
	 */
	@Override
	public void saveServerMemberPresenceStatus(Long serverId, Long memberId, ServerMemberStatus status) {
		String key = ServerRedisKeys.getServerMemberPresenceStatusKey(serverId);
		redisTemplate.opsForHash().put(key, memberId, status);
	}

	// 서버별 멤버 상태 정보 삭제
	@Override
	public void removeServerMemberPresenceStatus(Long serverId, Long memberId) {
		String key = ServerRedisKeys.getServerMemberPresenceStatusKey(serverId);
		redisTemplate.opsForHash().delete(key, memberId);
	}

	// 서버별 멤버 상태 정보 조회
	@Override
	public boolean existsServerMemberPresenceStatus(Long serverId) {
		String key = ServerRedisKeys.getServerMemberPresenceStatusKey(serverId);
		return Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}

	// 서버별 멤버 상태 정보 조회
	@Override
	public ServerMemberStatus getServerMemberStatus(Long serverId, Long memberId) {
		String key = ServerRedisKeys.getServerMemberPresenceStatusKey(serverId);
		Object obj = redisTemplate.opsForHash().get(key, memberId);
		if (obj instanceof ServerMemberStatus) {
			return (ServerMemberStatus) obj;
		}
		return null;
	}

	// 서버별 모든 멤버 상태 정보 조회
	@Override
	public List<ServerMemberStatus> getAllServerMemberStatus(Long serverId) {
		String key = ServerRedisKeys.getServerMemberPresenceStatusKey(serverId);
		List<Object> statusList = redisTemplate.opsForHash().values(key);
		return statusList.stream()
				.filter(obj -> obj instanceof ServerMemberStatus)
				.map(obj -> (ServerMemberStatus) obj)
				.collect(Collectors.toList());
	}

	// 서버 삭제에 따른 서버별  멤버 상태 정보 삭제
	@Override
	public void deleteServerMemberStatus(Long serverId) {
		String key = ServerRedisKeys.getServerMemberPresenceStatusKey(serverId);
		redisTemplate.delete(key);
	}

	/**
	 * 서버별 채널 목록을 Set 구조로 저장
	 * ex) serverChannel:{serverId}:channelList => Set<ChannelId>
	 */
	@Override
	public void saveServerChannelSet(Long serverId, List<Long> channelIdList) {
		String key = ServerRedisKeys.getServerChannelListKey(serverId);
		redisTemplate.opsForSet().add(key, channelIdList.toArray());
	}

	@Override
	public void addServerChannelToSet(Long serverId, Long channelId) {
		String key = ServerRedisKeys.getServerChannelListKey(serverId);
		redisTemplate.opsForSet().add(key, channelId);
	}

	@Override
	public void removeServerChannelFromSet(Long serverId, Long channelId) {
		String key = ServerRedisKeys.getServerChannelListKey(serverId);
		redisTemplate.opsForSet().remove(key, channelId);
	}

	@Override
	public boolean existsServerChannelList(Long serverId) {
		String key = ServerRedisKeys.getServerChannelListKey(serverId);
		return Boolean.TRUE.equals(redisTemplate.hasKey(key));
	}

	@Override
	public Set<Long> getServerChannelList(Long serverId) {
		String key = ServerRedisKeys.getServerChannelListKey(serverId);
		Set<Object> channelSet = redisTemplate.opsForSet().members(key);
		if (channelSet == null) {
			return Set.of();
		}
		return channelSet.stream()
				.map(this::convertToLong)
				.filter(Objects::nonNull)
				.collect(Collectors.toSet());
	}

	@Override
	public void deleteServerChannelList(Long serverId) {
		String key = ServerRedisKeys.getServerChannelListKey(serverId);
		redisTemplate.delete(key);
	}

	// 객체를 Long으로 변환 (내부 메서드)
	@Override
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
