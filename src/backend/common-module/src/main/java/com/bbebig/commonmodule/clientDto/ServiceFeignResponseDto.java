package com.bbebig.commonmodule.clientDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ServiceFeignResponseDto {

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ServerMemberListResponseDto {
		private Long serverId;
		private Long ownerId;
		private List<Long> memberIdList;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ServerChannelListResponseDto {
		private Long serverId;
		private List<Long> channelIdList;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class MemberServerListResponseDto {
		private Long memberId;
		private List<Long> serverIdList;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class DmMemberListResponseDto {
		private Long memberId;
		private List<Long> dmMemberIdList;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ServerLastInfoResponseDto {
		private Long serverId;
		private List<ChannelLastInfoResponseDto> channelInfoList;
	}

	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class ChannelLastInfoResponseDto {
		private Long channelId;
		private Long lastReadMessageId;
		private Long lastReadSequence;
		private LocalDateTime lastAccessAt;
	}

}
