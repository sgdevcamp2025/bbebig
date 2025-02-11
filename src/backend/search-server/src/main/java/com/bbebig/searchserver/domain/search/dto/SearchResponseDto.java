package com.bbebig.searchserver.domain.search.dto;

import com.bbebig.searchserver.domain.search.domain.ChannelChatMessage;
import com.bbebig.searchserver.domain.search.domain.ChannelChatMessageElastic;
import com.bbebig.searchserver.domain.search.domain.DmChatMessage;
import com.bbebig.searchserver.domain.search.domain.DmChatMessageElastic;
import lombok.Builder;
import lombok.Data;

import java.util.List;

public class SearchResponseDto {

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
