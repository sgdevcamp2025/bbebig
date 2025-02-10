package com.bbebig.searchserver.domain.search.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

public class ChatMessageResponseDto {

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
}
