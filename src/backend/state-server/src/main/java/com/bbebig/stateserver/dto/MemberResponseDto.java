package com.bbebig.stateserver.dto;

import lombok.Builder;
import lombok.Data;

public class MemberResponseDto {

	@Data
	@Builder
	public static class MemberGlobalStatusResponseDto {
		private String memberId;
		private String globalStatus;
	}
}
