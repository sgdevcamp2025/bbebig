package com.bbebig.stateserver.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeviceInfo {

	private String deviceId;

	private String platform;

	private String socketSessionId;

	private String connectedServerIp;

	private String lastActiveTime;

	private String currentRoomType;

	private String currentChannelId;

	private String currentServerId;

	public void updateCurrent(String currentRoomType, String currentChannelId, String currentServerId) {
		this.currentRoomType = currentRoomType;
		this.currentChannelId = currentChannelId;
		this.currentServerId = currentServerId;
	}
}
