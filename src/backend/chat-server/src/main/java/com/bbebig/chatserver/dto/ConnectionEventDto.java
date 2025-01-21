package com.bbebig.chatserver.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ConnectionEventDto {
	private long memberId;
	private String type;
	private String currentPresenceStatus;
	private String customPresenceStatus;
	private String socketSessionId;
	private String connectedServerIp;
	private long currentServerId;
	private long currentChannelId;
	private List<Long> serverIds;
	private List <Long> channelIds;
	private List <Long> dmChannelIds;
}
