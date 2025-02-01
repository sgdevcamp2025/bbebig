package com.bbebig.stateserver.domain;

import com.bbebig.commonmodule.kafka.dto.model.PresenceType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class MemberPresenceStatus {

	private Long memberId;

	private PresenceType globalStatus;

	private PresenceType actualStatus;

	private LocalDateTime lastActivityTime;

	private List<DeviceInfo> devices;

	public void updateLastActivityTime(LocalDateTime lastActivityTime) {
		this.lastActivityTime = lastActivityTime;
	}

	public void updateGlobalStatus(PresenceType globalStatus) {
		this.globalStatus = globalStatus;
	}

	public void updateActualStatus(PresenceType actualStatus) {
		this.actualStatus = actualStatus;
	}
}
