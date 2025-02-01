package com.bbebig.searchserver.domain.search.controller;

import com.bbebig.searchserver.domain.search.domain.ChannelChatMessageElastic;
import com.bbebig.searchserver.domain.search.domain.DmChatMessageElastic;
import com.bbebig.searchserver.domain.search.service.SearchService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.bbebig.searchserver.domain.search.dto.SearchRequestDto.*;

@Slf4j
@RestController("/search-server")
@RequiredArgsConstructor
public class SearchController {

	private final SearchService searchService;

	// TODO: 다중 검색 옵션을 지원하는 기능 구현

	@PostMapping("/search/server/{serverId}")
	public Page<ChannelChatMessageElastic> searchChannelMessagesByContentAndDate(
			@RequestBody SearchMessageRequestDto requestDto, @PathVariable Long serverId) {
		log.info("[Search] SearchController: 키워드 기반 채널 채팅 검색 요청. serverID: {}, keyword: {}", serverId, requestDto.getKeyword());
		return searchService.searchChannelMessagesByOptions(requestDto, serverId);
	}

	// 기본 DM 채팅 검색
	@PostMapping("/search/dm/{channelId}")
	public Page<DmChatMessageElastic> searchDmMessagesByContent(
			@RequestBody SearchMessageRequestDto requestDto, @PathVariable Long channelId) {
		log.info("[Search] SearchController: 키워드 기반 DM 채팅 검색 요청. channelID: {}, keyword: {}", channelId, requestDto.getKeyword());
		return searchService.searchDmMessagesByOptions(requestDto, channelId);
	}

}
