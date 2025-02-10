package com.bbebig.searchserver.domain.search.controller;

import com.bbebig.commonmodule.global.response.code.CommonResponse;
import com.bbebig.searchserver.domain.search.dto.SearchResponseDto.*;
import com.bbebig.searchserver.domain.search.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController("/message")
@RequiredArgsConstructor
public class ChatMessageServiceController {

	private final SearchService searchService;

	@Operation(summary = "서버 채팅 채널 과거 메시지 조회", description = "마지막 MessageId를 기반으로 원하는 수량만큼 과거 메시지를 조회합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "과거 메시지 조회 성공", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "400", description = "", content = @Content)
	})
	@GetMapping("/server/{serverId}/channel/{channelId}/messages")
	public CommonResponse<GetChannelMessageResponseDto> getServerChannelMessageByChannelId(@PathVariable Long serverId, @PathVariable Long channelId,
																						   @RequestParam(required = false) Long messageId,
																						   @RequestParam(defaultValue = "300") int limit) {
		log.info("[Search] ChatMessageServiceController: 채널 ID로 메시지 검색 요청. serverID: {}, channelID: {}", serverId, channelId);
		return CommonResponse.onSuccess(searchService.getChannelMessages(serverId, channelId, messageId, limit));
	}

	@Operation(summary = "DM 채팅 과거 메시지 조회", description = "마지막 MessageId를 기반으로 원하는 수량만큼 과거 메시지를 조회합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "과거 메시지 조회 성공", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "400", description = "", content = @Content)
	})
	@GetMapping("/dm/{channelId}/messages")
	public CommonResponse<GetDmMessageResponseDto> getDmChannelMessageByChannelId(@PathVariable Long channelId,
																				  @RequestParam(required = false) Long messageId,
																				  @RequestParam(defaultValue = "300") int limit) {
		log.info("[Search] ChatMessageServiceController: DM 채널 ID로 메시지 검색 요청. channelID: {}", channelId);
		return CommonResponse.onSuccess(searchService.getDmChannelMessages(channelId, messageId, limit));
	}

	@Operation(summary = "멤버별 서버 안읽은 메시지 수 조회", description = "멤버 서버별로 안읽은 메시지 수 조회")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "멤버별 서버 안읽은 메시지 수 조회 성공", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "400", description = "", content = @Content)
	})
	@GetMapping("/member/{memberId}/server/unread")
	public CommonResponse<?> getMemberServerUnreadCount(@PathVariable Long memberId) {
		log.info("[Search] ChatMessageServiceController: 멤버별 서버 안읽은 메시지 수 조회 요청. memberId: {}", memberId);
		return CommonResponse.onSuccess(null);
	}

	@Operation(summary = "멤버별 DM 안읽은 메시지 수 조회", description = "멤버 DM별로 안읽은 메시지 수 조회")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "멤버별 DM 안읽은 메시지 수 조회 성공", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "400", description = "", content = @Content)
	})
	@GetMapping("/member/{memberId}/dm/unread")
	public CommonResponse<?> getMemberDmUnreadCount(@PathVariable Long memberId) {
		log.info("[Search] ChatMessageServiceController: 멤버별 DM 안읽은 메시지 수 조회 요청. memberId: {}", memberId);
		return CommonResponse.onSuccess(null);
	}
}
