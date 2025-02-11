package com.bbebig.searchserver.domain.search.dto;

import com.bbebig.searchserver.domain.search.domain.ChannelChatMessage;
import com.bbebig.searchserver.domain.search.domain.DmChatMessage;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class HistoryResponseDto {

	@Data
	@Builder
	public static class AllServerUnreadCountDto {
		private Long memberId;
		private int totalServerCount;
		private List<ServerUnreadCountDto> serverInfos;
	}

	@Data
	@Builder
	public static class ServerUnreadCountDto {
		private Long serverId;
		private List<ChannelUnreadCountDto> channels;
		private int totalUnread;
	}

	@Data
	@Builder
	public static class ChannelUnreadCountDto {
		private Long channelId;
		private int unreadCount;
	}

	@Data
	@Builder
	public static class GetChannelMessageResponseDto {
		private Long serverId;
		private Long channelId;
		private Long lastMessageId;
		private int totalCount;
		private List<ChannelChatMessage> messages;
	}

	@Data
	@Builder
	public static class GetDmMessageResponseDto {
		private Long channelId;
		private Long lastMessageId;
		private int totalCount;
		private List<DmChatMessage> messages;
	}
}
