package com.bbebig.chatserver.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SessionEventDto {
	private long memberId;
	private String type;
	private String currentStatus;
	private String socketSessionId;
	private String connectedServerIp;
}
