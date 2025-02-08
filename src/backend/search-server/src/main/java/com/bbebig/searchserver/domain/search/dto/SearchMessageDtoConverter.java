package com.bbebig.searchserver.domain.search.dto;

import com.bbebig.searchserver.domain.search.domain.ChannelChatMessage;
import com.bbebig.searchserver.domain.search.domain.ChannelChatMessageElastic;
import com.bbebig.searchserver.domain.search.domain.DmChatMessage;
import com.bbebig.searchserver.domain.search.domain.DmChatMessageElastic;
import org.springframework.data.domain.Page;

import java.util.List;

import static com.bbebig.searchserver.domain.search.dto.SearchResponseDto.*;

public class SearchMessageDtoConverter {

	public static SearchChannelMessageResponseDto converToSearchChannelMessageResponseDto(Page<ChannelChatMessageElastic> page, Long serverId) {
		return SearchChannelMessageResponseDto.builder()
				.serverId(serverId)
				.totalCount((int) page.getTotalElements())
				.messages(page.getContent())
				.build();
	}

	public static SearchDmMessageResponseDto convertToSearchDmMessageResponseDto(Page<DmChatMessageElastic> page, Long channelId) {
		return SearchDmMessageResponseDto.builder()
				.channelId(channelId)
				.totalCount((int) page.getTotalElements())
				.messages(page.getContent())
				.build();
	}

	public static GetChannelMessageResponseDto convertToGetChannelMessageResponseDto(Long serverId, Long channelId, List<ChannelChatMessage> messages) {
		return GetChannelMessageResponseDto.builder()
				.serverId(serverId)
				.channelId(channelId)
				.lastMessageId(messages.get(messages.size() - 1).getId())
				.totalCount(messages.size())
				.messages(messages)
				.build();
	}

	public static GetDmMessageResponseDto convertToGetDmMessageResponseDto(Long channelId, List<DmChatMessage> messages) {
		return GetDmMessageResponseDto.builder()
				.channelId(channelId)
				.lastMessageId(messages.get(messages.size() - 1).getId())
				.totalCount(messages.size())
				.messages(messages)
				.build();
	}
}
