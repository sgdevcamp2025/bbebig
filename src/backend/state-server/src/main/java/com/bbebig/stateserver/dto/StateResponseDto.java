package com.bbebig.stateserver.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

public class StateResponseDto {

	@Data
	@Builder
	public static class MemberStatusResponseDto {
		private Long memberId;
		private String globalStatus;
		private String actualStatus;
	}

	@Data
	@Builder
	public static class ServerMemberPresenceResponseDto {
		private long serverId;
		private List<MemberPresenceStatusDto> memberPresenceStatusList;
	}

	@Data
	@Builder
	public static class MemberPresenceStatusDto {
		private Long memberId;
		private String globalStatus;
		private String actualStatus;
	}
}
