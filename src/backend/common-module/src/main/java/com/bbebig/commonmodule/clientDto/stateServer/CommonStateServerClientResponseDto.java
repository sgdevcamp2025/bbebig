package com.bbebig.commonmodule.clientDto.stateServer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class CommonStateServerClientResponseDto {

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MemberServerListCacheResponseDto {
		private Long memberId;
		private Long serverId;
		List<Long> serverIdList;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MemberDmListCacheResponseDto {
		private Long memberId;
		private Long dmMemberId;
		List<Long> dmChannelIdList;
	}
}
