package com.bbebig.commonmodule.clientDto.serviceServer;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class CommonServiceServerClientResponseDto {

	@Data
	@Builder
	public static class ServerMemberListResponseDto {
		private Long serverId;
		private Long ownerId;
		private List<Long> memberIdList;
	}

	@Data
	@Builder
	public static class ServerChannelListResponseDto {
		private Long serverId;
		private List<Long> channelIdList;
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

	@Data
	@Builder
	public static class ServerLastInfoResponseDto {
		private Long serverId;
		private List<ChannelLastInfoResponseDto> channelInfoList;
	}

	@Data
	@Builder
	public static class ChannelLastInfoResponseDto {
		private Long channelId;
		private Long lastReadMessageId;
		private LocalDateTime lastAccessAt;
	}

}
