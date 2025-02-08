package com.bbebig.searchserver.domain.search.dto;

import com.bbebig.searchserver.domain.search.domain.ChannelChatMessage;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class SearchResponseDto {

	@Data
	@Builder
	public static class GetChannelMessageResponseDto {
		private Long serverId;
		private Long channelId;
		private Long lastMessageId;
		private int count;
		private List<ChannelChatMessage> messages;
	}

}
