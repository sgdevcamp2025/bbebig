package com.bbebig.commonmodule.redis.domain;

import com.bbebig.commonmodule.kafka.dto.model.PresenceType;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberPresenceStatus {

	private Long memberId;

	private PresenceType globalStatus;

	private PresenceType actualStatus;

	private PresenceType customStatus;

	private LocalDateTime lastActivityTime;

	private List<DeviceInfo> devices;

	public void updateLastActivityTime(LocalDateTime lastActivityTime) {
		this.lastActivityTime = lastActivityTime;
	}

	public void updateActualStatus(PresenceType actualStatus) {
		this.actualStatus = actualStatus;
		calculateGlobalStatus();
	}

	public boolean existsDevice(String sessionId) {
		for (DeviceInfo deviceInfo : devices) {
			if (deviceInfo.getSocketSessionId().equals(sessionId)) {
				return true;
			}
		}
		return false;
	}

	public void updateCustomStatus(PresenceType customStatus) {
		this.customStatus = customStatus;
		calculateGlobalStatus();
	}

	public void deleteDevice(String sessionId) {
		if (devices == null || devices.isEmpty()) {
			return;
		}
		devices.removeIf(deviceInfo -> deviceInfo.getSocketSessionId().equals(sessionId));
		calculateGlobalStatus();
	}

	private void calculateGlobalStatus() {
		if (devices == null || devices.isEmpty() || actualStatus == PresenceType.OFFLINE) {
			actualStatus = PresenceType.OFFLINE;
			globalStatus = PresenceType.OFFLINE;
			return;
		} else {
			actualStatus = PresenceType.ONLINE;
			globalStatus = Objects.requireNonNullElse(customStatus, PresenceType.ONLINE);
			if (globalStatus == PresenceType.INVISIBLE) {
				globalStatus = PresenceType.OFFLINE;
			}
		}

	}
}
