package com.bbebig.commonmodule.redis.domain;

import com.bbebig.commonmodule.kafka.dto.model.ChannelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
