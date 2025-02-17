package com.bbebig.stateserver.repository;

import com.bbebig.commonmodule.redis.domain.ServerMemberStatus;
import com.bbebig.commonmodule.redis.repository.ServerRedisRepository;
import com.bbebig.commonmodule.redis.util.ServerRedisKeys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ServerRedisRepositoryImpl implements ServerRedisRepository {

	private final RedisTemplate<String, Long> redisSetTemplate;
	private final RedisTemplate<String, ServerMemberStatus> redisServerStatusTemplate;

	private SetOperations<String, Long> setOperations;
	private HashOperations<String, Long, ServerMemberStatus> hashOperations;

	@PostConstruct
	public void initRedisOps() {
		this.setOperations = redisSetTemplate.opsForSet();
		this.hashOperations = redisServerStatusTemplate.opsForHash();
	}

	/**
	 * 서버에 참여하고 있는 멤버 목록을 Set 구조로 저장
	 * ex) server:{serverId}:serverMemberList => Set<MemberId>
	 */
	@Override
	public void saveServerMemberSet(Long serverId, List<Long> memberIdList) {
		String key = ServerRedisKeys.getServerMemberListKey(serverId);
		setOperations.add(key, memberIdList.toArray(new Long[0]));
	}

	@Override
	public void addServerMemberToSet(Long serverId, Long memberId) {
		String key = ServerRedisKeys.getServerMemberListKey(serverId);
		setOperations.add(key, memberId);
	}

	@Override
	public void removeServerMemberFromSet(Long serverId, Long memberId) {
		String key = ServerRedisKeys.getServerMemberListKey(serverId);
		setOperations.remove(key, memberId);
	}

	@Override
	public boolean existsServerMemberList(Long serverId) {
		String key = ServerRedisKeys.getServerMemberListKey(serverId);
		return Boolean.TRUE.equals(redisSetTemplate.hasKey(key));
	}

	@Override
	public Set<Long> getServerMemberList(Long serverId) {
		String key = ServerRedisKeys.getServerMemberListKey(serverId);
		return setOperations.members(key);
	}

	@Override
	public void deleteServerMemberList(Long serverId) {
		String key = ServerRedisKeys.getServerMemberListKey(serverId);
		redisSetTemplate.delete(key);
	}

	/**
	 * 서버별 멤버 상태 정보를 Hash 구조로 저장
	 * (server:{serverId}:serverMemberStatus) => ServerMemberStatus
	 */
	@Override
	public void saveServerMemberPresenceStatus(Long serverId, Long memberId, ServerMemberStatus status) {
		String key = ServerRedisKeys.getServerMemberPresenceStatusKey(serverId);
		hashOperations.put(key, memberId, status);
	}

	@Override
	public void removeServerMemberPresenceStatus(Long serverId, Long memberId) {
		String key = ServerRedisKeys.getServerMemberPresenceStatusKey(serverId);
		hashOperations.delete(key, memberId);
	}

	@Override
	public boolean existsServerMemberPresenceStatus(Long serverId) {
		String key = ServerRedisKeys.getServerMemberPresenceStatusKey(serverId);
		return Boolean.TRUE.equals(redisServerStatusTemplate.hasKey(key));
	}

	@Override
	public ServerMemberStatus getServerMemberStatus(Long serverId, Long memberId) {
		String key = ServerRedisKeys.getServerMemberPresenceStatusKey(serverId);
		return hashOperations.get(key, memberId);
	}

	@Override
	public java.util.List<ServerMemberStatus> getAllServerMemberStatus(Long serverId) {
		String key = ServerRedisKeys.getServerMemberPresenceStatusKey(serverId);
		return hashOperations.values(key);
	}

	@Override
	public void deleteServerMemberStatus(Long serverId) {
		String key = ServerRedisKeys.getServerMemberPresenceStatusKey(serverId);
		redisServerStatusTemplate.delete(key);
	}

	/**
	 * 서버에 참여하고 있는 채널 목록을 Set 구조로 저장
	 * ex) server:{serverId}:channelList => Set<ChannelId>
	 */
	@Override
	public void saveServerChannelSet(Long serverId, List<Long> channelIdList) {
		String key = ServerRedisKeys.getServerChannelListKey(serverId);
		setOperations.add(key, channelIdList.toArray(new Long[0]));
	}

	@Override
	public void addServerChannelToSet(Long serverId, Long channelId) {
		String key = ServerRedisKeys.getServerChannelListKey(serverId);
		setOperations.add(key, channelId);
	}

	@Override
	public void removeServerChannelFromSet(Long serverId, Long channelId) {
		String key = ServerRedisKeys.getServerChannelListKey(serverId);
		setOperations.remove(key, channelId);
	}

	@Override
	public boolean existsServerChannelList(Long serverId) {
		String key = ServerRedisKeys.getServerChannelListKey(serverId);
		return Boolean.TRUE.equals(redisSetTemplate.hasKey(key));
	}

	@Override
	public Set<Long> getServerChannelList(Long serverId) {
		String key = ServerRedisKeys.getServerChannelListKey(serverId);
		return setOperations.members(key);
	}

	@Override
	public void deleteServerChannelList(Long serverId) {
		String key = ServerRedisKeys.getServerChannelListKey(serverId);
		redisSetTemplate.delete(key);
	}
}
