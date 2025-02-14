package com.bbebig.commonmodule.kafka.dto.serverEvent;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class ServerEventDto {

	Long serverId;

	ServerEventType type;  // ServerEventType Enum 참고

	@JsonCreator
	public ServerEventDto(
			@JsonProperty("serverId") Long serverId,
			@JsonProperty("type") ServerEventType type
	) {
		this.serverId = serverId;
		this.type = type;
	}
}