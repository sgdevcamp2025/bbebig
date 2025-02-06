package com.bbebig.commonmodule.kafka.dto;

import com.bbebig.commonmodule.kafka.dto.model.ChannelType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ConnectionEventDto {

	private Long memberId;

	// CONNECT, DISCONNECT
	private String type;

	private String socketSessionId;

	private String connectedServerIp;

	private String platform;

	private String deviceType;

	// Andorid인 경우 (로컬에 정보가 있기 때문에 한번에 요청 가능)\
	private ChannelType currentChannelType;

	private Long currentChannelId;

	private Long currentServerId;

	@JsonCreator
	public ConnectionEventDto(
			@JsonProperty("memberId") Long memberId,
			@JsonProperty("type") String type,
			@JsonProperty("socketSessionId") String socketSessionId,
			@JsonProperty("connectedServerIp") String connectedServerIp,
			@JsonProperty("platform") String platform,
			@JsonProperty("deviceType") String deviceType,
			@JsonProperty("currentChannelType") ChannelType currentChannelType,
			@JsonProperty("currentChannelId") Long currentChannelId,
			@JsonProperty("currentServerId") Long currentServerId
	) {
		this.memberId = memberId;
		this.type = type;
		this.socketSessionId = socketSessionId;
		this.connectedServerIp = connectedServerIp;
		this.platform = platform;
		this.deviceType = deviceType;
		this.currentChannelType = currentChannelType;
		this.currentChannelId = currentChannelId;
		this.currentServerId = currentServerId;
	}

}
