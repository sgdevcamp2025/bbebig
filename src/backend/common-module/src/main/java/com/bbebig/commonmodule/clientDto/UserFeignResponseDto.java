package com.bbebig.commonmodule.clientDto;


import com.bbebig.commonmodule.kafka.dto.model.PresenceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class UserFeignResponseDto {

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MemberGlobalStatusResponseDto {
		private Long memberId;
		private PresenceType globalStatus;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
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
