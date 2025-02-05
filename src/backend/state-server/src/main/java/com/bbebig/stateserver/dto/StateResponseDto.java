package com.bbebig.stateserver.dto;

import lombok.Builder;
import lombok.Data;

public class StateResponseDto {

	@Data
	@Builder
	public static class MemberStatusResponseDto {
		private long memberId;
		private String globalStatus;
		private String actualStatus;
	}
}
