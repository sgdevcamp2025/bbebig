package com.bbebig.commonmodule.kafka.dto;

import com.bbebig.commonmodule.kafka.dto.model.ChannelType;
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

}
