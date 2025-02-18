package com.bbebig.commonmodule.kafka.dto.serverEvent;

import com.bbebig.commonmodule.kafka.dto.serverEvent.status.ServerActionStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper=false)
public class ServerActionEventDto extends ServerEventDto {

	private String serverName;

	private String profileImageUrl;

	private ServerActionStatus status; // CREATE, UPDATE, DELETE

	@JsonCreator
	public ServerActionEventDto(
			@JsonProperty("serverId") Long serverId,
			@JsonProperty("type") ServerEventType type,
			@JsonProperty("serverName") String serverName,
			@JsonProperty("profileImageUrl") String profileImageUrl,
			@JsonProperty("status") ServerActionStatus status
	) {
		super(serverId, type);
		this.serverName = serverName;
		this.profileImageUrl = profileImageUrl;
		this.status = status;
	}

}
