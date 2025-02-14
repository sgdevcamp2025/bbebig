package com.bbebig.commonmodule.clientDto.stateServer;

import lombok.Builder;
import lombok.Data;

import java.util.List;

public class CommonStateServerClientResponseDto {

	@Data
	@Builder
	public static class MemberServerListCacheResponseDto {
		private Long memberId;
		private Long serverId;
		List<Long> serverIdList;
	}

	@Data
	@Builder
	public static class MemberDmListCacheResponseDto {
		private Long memberId;
		private Long dmMemberId;
		List<Long> dmChannelIdList;
	}
}
