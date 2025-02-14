package com.bbebig.commonmodule.redis.domain;

import com.bbebig.commonmodule.kafka.dto.model.PresenceType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServerMemberStatus {

	private Long memberId;
	private PresenceType actualStatus;            // "ONLINE", "OFFLINE", "DND", ...
	private PresenceType globalStatus;      // if user is DND or INVISIBLE, etc.

}
