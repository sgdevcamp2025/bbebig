package com.bbebig.commonmodule.kafka.dto.serverEvent;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServerMemberPresenceEventDto extends ServerEventDto {

	private Long memberId;

	private String actualStatus; // ONLINE, OFFLINE, AWAY, BUSY, INVISIBLE

	private String globalStatus; // ONLINE, OFFLINE, AWAY, BUSY, INVISIBLE
}
