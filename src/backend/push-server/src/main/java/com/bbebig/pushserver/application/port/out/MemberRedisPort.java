package com.bbebig.pushserver.application.port.out;

import com.bbebig.commonmodule.redis.domain.MemberPresenceStatus;
import com.bbebig.commonmodule.redis.domain.ServerLastInfo;

public interface MemberRedisPort {

	MemberPresenceStatus getMemberPresenceStatus(Long memberId);

	boolean existsMemberPresenceStatus(Long memberId);

	ServerLastInfo getServerLastInfo(Long memberId, Long serverId);

	boolean existsServerLastInfo(Long memberId, Long serverId);
}
