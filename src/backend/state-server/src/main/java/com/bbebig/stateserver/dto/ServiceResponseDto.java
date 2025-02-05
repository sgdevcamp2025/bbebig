package com.bbebig.stateserver.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

public class ServiceResponseDto {

	@Data
	@Builder
	public static class ServerMemberListResponseDto {
		private Long memberId;
		private Long ownerId;
		private List<Long> memberIdList;
	}

	@Data
	@Builder
	public static class MemberServerListResponseDto {
		private Long memberId;
		private List<Long> serverIdList;
	}

	@Data
	@Builder
	public static class DmMemberListResponseDto {
		private Long memberId;
		private List<Long> dmMemberIdList;
	}
}
