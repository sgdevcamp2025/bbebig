package com.bbebig.commonmodule.clientDto;

import com.bbebig.commonmodule.kafka.dto.model.PresenceType;
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

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ServerMemberPresenceResponseDto {
		private Long serverId;
		private List<MemberPresenceResponseDto> memberPresenceStatusList;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MemberPresenceResponseDto {
		private Long memberId;
		private PresenceType globalStatus;
		private PresenceType actualStatus;
		private PresenceType customStatus;
	}


}
