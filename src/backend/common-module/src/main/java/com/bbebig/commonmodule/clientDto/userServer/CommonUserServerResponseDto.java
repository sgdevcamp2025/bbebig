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

	@Data
	@Builder
	public static class MemberInfoResponseDto {
		private Long memberId;
		private String name;
		private String nickname;
		private String email;
		private String avatarUrl;
		private String bannerUrl;
		private String introduce;
		private PresenceType globalStatus;
	}
}
