package com.bbebig.commonmodule.kafka.dto.serverEvent;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper=false)
public class ServerMemberPresenceEventDto extends ServerEventDto {

	private Long memberId;

	private String actualStatus; // ONLINE, OFFLINE, AWAY, BUSY, INVISIBLE

	private String globalStatus; // ONLINE, OFFLINE, AWAY, BUSY, INVISIBLE
}
