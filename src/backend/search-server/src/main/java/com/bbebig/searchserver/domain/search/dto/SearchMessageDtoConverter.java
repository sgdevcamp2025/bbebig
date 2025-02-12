package com.bbebig.searchserver.domain.search.dto;

import com.bbebig.searchserver.domain.search.domain.ChannelChatMessageElastic;
import com.bbebig.searchserver.domain.search.domain.DmChatMessageElastic;
import org.springframework.data.domain.Page;

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
}
