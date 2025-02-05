package com.bbebig.stateserver.domain;

import com.bbebig.commonmodule.kafka.dto.model.ChannelType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class DeviceInfo {

//	private String deviceId;

	private String platform;

	private String socketSessionId;

	private String connectedServerIp;

	private LocalDateTime lastActiveTime;

	private ChannelType currentChannelType;

	private Long currentChannelId;

	private Long currentServerId;

	public void updateCurrent(ChannelType currentChannelType, Long currentChannelId, Long currentServerId) {
		this.currentChannelType = currentChannelType;
		this.currentChannelId = currentChannelId;
		this.currentServerId = currentServerId;
	}

	public void updateLastActiveTime(LocalDateTime lastActiveTime) {
		this.lastActiveTime = lastActiveTime;
	}
}
