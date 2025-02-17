package com.bbebig.userserver.global.feign.clientDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class StateFeignResponseDto {

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
