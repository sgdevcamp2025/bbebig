package com.bbebig.searchserver.domain.search.dto;

import com.bbebig.searchserver.domain.search.domain.ChannelChatMessage;
import com.bbebig.searchserver.domain.search.domain.ChannelChatMessageElastic;
import com.bbebig.searchserver.domain.search.domain.DmChatMessage;
import com.bbebig.searchserver.domain.search.domain.DmChatMessageElastic;
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

	@Builder
	@Data
	public static class SearchChannelMessageResponseDto {
		private Long serverId;
		private int totalCount;
		private List<ChannelChatMessageElastic> messages;
	}

	@Builder
	@Data
	public static class SearchDmMessageResponseDto {
		private Long channelId;
		private int totalCount;
		private List<DmChatMessageElastic> messages;
	}



}
