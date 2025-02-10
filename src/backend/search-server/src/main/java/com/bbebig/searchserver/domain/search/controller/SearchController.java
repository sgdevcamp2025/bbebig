package com.bbebig.searchserver.domain.search.controller;

import com.bbebig.commonmodule.global.response.code.CommonResponse;
import com.bbebig.searchserver.domain.search.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.bbebig.searchserver.domain.search.dto.SearchRequestDto.*;
import static com.bbebig.searchserver.domain.search.dto.SearchResponseDto.*;

@Slf4j
@RestController("/search")
@RequiredArgsConstructor
public class SearchController {

	private final SearchService searchService;

	@Operation(summary = "서버 채팅 채널 검색", description = "요청한 검색 조건을 기반으로 채널 채팅을 검색합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "과거 메시지 조회 성공", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "400", description = "", content = @Content)
	})
	@PostMapping("/server/{serverId}")
	public CommonResponse<SearchChannelMessageResponseDto> searchChannelMessagesByContentAndDate(
			@RequestBody SearchMessageRequestDto requestDto, @PathVariable Long serverId) {
		log.info("[Search] SearchController: 키워드 기반 채널 채팅 검색 요청. serverID: {}, keyword: {}", serverId, requestDto.getKeyword());
		return CommonResponse.onSuccess(searchService.searchChannelMessagesByOptions(requestDto, serverId));
	}


	@Operation(summary = "DM 채팅 검색", description = "요청한 검색 조건을 기반으로 DM 채팅을 검색합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "과거 메시지 조회 성공", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "400", description = "", content = @Content)
	})
	@PostMapping("/dm/{channelId}")
	public CommonResponse<SearchDmMessageResponseDto> searchDmMessagesByContent(
			@RequestBody SearchMessageRequestDto requestDto, @PathVariable Long channelId) {
		log.info("[Search] SearchController: 키워드 기반 DM 채팅 검색 요청. channelID: {}, keyword: {}", channelId, requestDto.getKeyword());
		return CommonResponse.onSuccess(searchService.searchDmMessagesByOptions(requestDto, channelId));
	}

}
