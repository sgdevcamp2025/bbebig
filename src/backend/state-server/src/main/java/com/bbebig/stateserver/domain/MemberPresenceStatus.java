package com.bbebig.stateserver.domain;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MemberPresenceStatus {

	private String globalStatus;

	private String actualStatus;

	private String lastActivityTime;

	private List<DeviceInfo> devices;
}
