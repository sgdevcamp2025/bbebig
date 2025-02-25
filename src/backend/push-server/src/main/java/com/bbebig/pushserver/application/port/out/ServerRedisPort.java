package com.bbebig.pushserver.application.port.out;

import com.bbebig.commonmodule.redis.domain.ServerMemberStatus;

import java.util.List;
import java.util.Set;

public interface ServerRedisPort {

	Set<Long> getServerMembers(Long serverId);

	List<ServerMemberStatus> getAllServerMemberStatus(Long serverId);

	boolean existsServerMemberStatus(Long serverId);
}
