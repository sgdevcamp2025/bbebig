package com.bbebig.pushserver.adapter.out;

import com.bbebig.commonmodule.redis.domain.MemberPresenceStatus;
import com.bbebig.commonmodule.redis.domain.ServerLastInfo;
import com.bbebig.commonmodule.redis.util.MemberRedisKeys;
import com.bbebig.pushserver.application.port.out.MemberRedisPort;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class MemberRedisRepository implements MemberRedisPort {

	private final RedisTemplate<String, MemberPresenceStatus> redisMemberStatusTemplate;
	private ValueOperations<String, MemberPresenceStatus> valueOperations;

	private final RedisTemplate<String, ServerLastInfo> redisServerLastInfoTemplate;
	private HashOperations<String, String, ServerLastInfo> serverLastInfoValueOperations;

	@PostConstruct
	public void initRedisOps() {
		this.valueOperations = redisMemberStatusTemplate.opsForValue();
		this.serverLastInfoValueOperations = redisServerLastInfoTemplate.opsForHash();
	}

	@Override
	public MemberPresenceStatus getMemberPresenceStatus(Long memberId) {
		String key = MemberRedisKeys.getMemberStatusKey(memberId);
		return valueOperations.get(key);
	}

	@Override
	public boolean existsMemberPresenceStatus(Long memberId) {
		String key = MemberRedisKeys.getMemberStatusKey(memberId);
		return Boolean.TRUE.equals(valueOperations.get(key) != null);
	}


	@Override
	public ServerLastInfo getServerLastInfo(Long memberId, Long serverId) {
		String key = MemberRedisKeys.getServerLastInfoKey(memberId);
		return serverLastInfoValueOperations.get(key, serverId.toString());
	}

	@Override
	public boolean existsServerLastInfo(Long memberId, Long serverId) {
		String key = MemberRedisKeys.getMemberServerListKey(memberId);
		return Boolean.TRUE.equals(serverLastInfoValueOperations.hasKey(key, serverId.toString()));
	}
}
