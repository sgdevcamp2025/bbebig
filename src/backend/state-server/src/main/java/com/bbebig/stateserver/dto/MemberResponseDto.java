package com.bbebig.stateserver.dto;

import com.bbebig.commonmodule.kafka.dto.model.PresenceType;
import lombok.Builder;
import lombok.Data;

public class MemberResponseDto {

	@Data
	@Builder
	public static class MemberGlobalStatusResponseDto {
		private String memberId;
		private PresenceType globalStatus;
	}
}
