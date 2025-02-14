package com.bbebig.commonmodule.kafka.dto.serverEvent;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper=false)
public class ServerActionEventDto extends ServerEventDto {

	private String serverName;

	private String profileImageUrl;

	String status; // CREATE, UPDATE, DELETE

	@JsonCreator
	public ServerActionEventDto(
			@JsonProperty("serverId") Long serverId,
			@JsonProperty("serverEventType") ServerEventType serverEventType,
			@JsonProperty("serverName") String serverName,
			@JsonProperty("profileImageUrl") String profileImageUrl,
			@JsonProperty("status") String status
	) {
		super(serverId, serverEventType);
		this.serverName = serverName;
		this.profileImageUrl = profileImageUrl;
		this.status = status;
	}

}
