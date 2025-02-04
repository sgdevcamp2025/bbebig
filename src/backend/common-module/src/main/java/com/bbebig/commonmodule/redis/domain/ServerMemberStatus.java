package com.bbebig.commonmodule.redis.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServerMemberStatus {

	private String actualStatus;            // "ONLINE", "OFFLINE", "DND", ...
	private String globalStatus;      // if user is DND or INVISIBLE, etc.

}
