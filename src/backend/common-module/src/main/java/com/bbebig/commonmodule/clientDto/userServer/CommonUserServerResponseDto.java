package com.bbebig.commonmodule.clientDto.userServer;


import com.bbebig.commonmodule.kafka.dto.model.PresenceType;
import lombok.Builder;
import lombok.Data;

public class CommonUserServerResponseDto {

	@Data
	@Builder
	public static class MemberGlobalStatusResponseDto {
		private Long memberId;
		private PresenceType globalStatus;
	}
}
