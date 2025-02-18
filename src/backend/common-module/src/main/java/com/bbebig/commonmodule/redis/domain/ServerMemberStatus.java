package com.bbebig.commonmodule.redis.domain;

import com.bbebig.commonmodule.kafka.dto.model.PresenceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerMemberStatus implements Serializable {

	private Long memberId;
	private PresenceType actualStatus;            // "ONLINE", "OFFLINE", "DND", ...
	private PresenceType globalStatus;      // if user is DND or INVISIBLE, etc.

}
