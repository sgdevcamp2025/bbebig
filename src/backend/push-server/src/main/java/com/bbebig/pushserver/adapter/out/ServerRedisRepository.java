package com.bbebig.pushserver.adapter.out;

import com.bbebig.commonmodule.redis.domain.ServerMemberStatus;
import com.bbebig.commonmodule.redis.util.ServerRedisKeys;
import com.bbebig.pushserver.application.port.out.ServerRedisPort;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
@RequiredArgsConstructor
public class ServerRedisRepository implements ServerRedisPort {

	private final RedisTemplate<String, Long> redisSetTemplate;
	private final RedisTemplate<String, ServerMemberStatus> redisServerStatusTemplate;

	private SetOperations<String, Long> setOperations;
	private HashOperations<String, String, ServerMemberStatus> hashOperations;

	@PostConstruct
	public void initRedisOps() {
		this.setOperations = redisSetTemplate.opsForSet();
		this.hashOperations = redisServerStatusTemplate.opsForHash();
	}


	@Override
	public Set<Long> getServerMembers(Long serverId) {
		String key = ServerRedisKeys.getServerMemberListKey(serverId);
		return setOperations.members(key);
	}

	@Override
	public List<ServerMemberStatus> getAllServerMemberStatus(Long serverId) {
		String key = ServerRedisKeys.getServerMemberPresenceStatusKey(serverId);
		if (!existsServerMemberStatus(serverId)) {
			return new ArrayList<>();
		}
		return hashOperations.values(key);
	}

	@Override
	public boolean existsServerMemberStatus(Long serverId) {
		String key = ServerRedisKeys.getServerMemberPresenceStatusKey(serverId);
		return Boolean.TRUE.equals(redisServerStatusTemplate.hasKey(key));
	}
}
