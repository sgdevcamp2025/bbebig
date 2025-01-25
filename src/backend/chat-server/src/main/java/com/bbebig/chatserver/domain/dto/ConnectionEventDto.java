package com.bbebig.chatserver.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ConnectionEventDto {

	private Long memberId;
	private String type;
	private String socketSessionId;
	private String connectedServerIp;
	private String platform;
	private String deviceId;
	private String deviceType;
	private String tabId;
	private String currentRoomType;
	private String currentChannelId;
	private String currentServerId;

}
