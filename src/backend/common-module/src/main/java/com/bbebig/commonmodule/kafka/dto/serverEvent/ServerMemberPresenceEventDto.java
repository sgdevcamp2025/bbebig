package com.bbebig.commonmodule.kafka.dto.serverEvent;

import com.bbebig.commonmodule.kafka.dto.model.PresenceType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper=false)
public class ServerMemberPresenceEventDto extends ServerEventDto {

	private Long memberId;

	private PresenceType actualStatus;

	private PresenceType globalStatus;

	@JsonCreator
	public ServerMemberPresenceEventDto(
			@JsonProperty("serverId") Long serverId,
			@JsonProperty("serverEventType") ServerEventType serverEventType,
			@JsonProperty("memberId") Long memberId,
			@JsonProperty("actualStatus") PresenceType actualStatus,
			@JsonProperty("globalStatus") PresenceType globalStatus
	) {
		super(serverId, serverEventType);
		this.memberId = memberId;
		this.actualStatus = actualStatus;
		this.globalStatus = globalStatus;
	}
}
