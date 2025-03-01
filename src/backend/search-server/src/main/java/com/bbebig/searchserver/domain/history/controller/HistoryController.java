package com.bbebig.searchserver.domain.history.controller;

import com.bbebig.commonmodule.global.response.code.CommonResponse;
import com.bbebig.commonmodule.kafka.dto.ChatMessageDto;
import com.bbebig.commonmodule.passport.annotation.PassportUser;
import com.bbebig.searchserver.domain.history.dto.HistoryResponseDto.*;
import com.bbebig.searchserver.domain.history.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.bbebig.commonmodule.proto.PassportProto.*;

@Slf4j
@RestController
@RequestMapping("/history")
@RequiredArgsConstructor
public class HistoryController {

	private final HistoryService historyService;

	@Operation(summary = "서버 채팅 채널 과거 메시지 조회", description = "마지막 MessageId를 기반으로 원하는 수량만큼 과거 메시지를 조회합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "과거 메시지 조회 성공", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "400", description = "", content = @Content)
	})
	@GetMapping("/server/{serverId}/channel/{channelId}/messages")
	public CommonResponse<GetChannelMessageResponseDto> getServerChannelMessageByChannelId(@PathVariable Long serverId, @PathVariable Long channelId,
																						   @RequestParam(required = false) Long sequence,
																						   @RequestParam(defaultValue = "300") int limit) {
		log.info("[Search] ChatMessageServiceController: 채널 ID로 메시지 검색 요청. serverID: {}, channelID: {}", serverId, channelId);
		return CommonResponse.onSuccess(historyService.getChannelMessages(serverId, channelId, sequence, limit));
	}

	@Operation(summary = "DM 채팅 과거 메시지 조회", description = "마지막 MessageId를 기반으로 원하는 수량만큼 과거 메시지를 조회합니다.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "과거 메시지 조회 성공", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "400", description = "", content = @Content)
	})
	@GetMapping("/dm/{channelId}/messages")
	public CommonResponse<GetDmMessageResponseDto> getDmChannelMessageByChannelId(@PathVariable Long channelId,
																				  @Parameter(hidden = true) @PassportUser Passport passport,
																				  @RequestParam(required = false) Long messageId,
																				  @RequestParam(defaultValue = "300") int limit) {
		log.info("[Search] ChatMessageServiceController: DM 채널 ID로 메시지 검색 요청. channelID: {}", channelId);
		return CommonResponse.onSuccess(historyService.getDmMessages(channelId, messageId, limit));
	}

	@Operation(summary = "멤버별 모든 서버 안읽은 메시지 수 조회", description = "멤버가 속한 모든 서버별로 안읽은 메시지 수 조회")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "멤버별 모든 서버 안읽은 메시지 수 조회 성공", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "400", description = "", content = @Content)
	})
	@GetMapping("/unread/server/all")
	public CommonResponse<AllServerUnreadCountDto> getMemberAllServerUnreadCount(@Parameter(hidden = true) @PassportUser Passport passport) {
		log.info("[Search] ChatMessageServiceController: 멤버별 서버 안읽은 메시지 수 조회 요청. memberId: {}", passport.getMemberId());
		return CommonResponse.onSuccess(historyService.getAllServerUnreadCount(passport.getMemberId()));
	}

	@Operation(summary = "멤버별 서버 안읽은 메시지 수 조회", description = "멤버가 속한 서버별로 안읽은 메시지 수 조회")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "멤버별 서버 안읽은 메시지 수 조회 성공", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "400", description = "", content = @Content)
	})
	@GetMapping("/unread/server/{serverId}")
	public CommonResponse<ServerUnreadCountDto> getMemberServerUnreadCount(@Parameter(hidden = true) @PassportUser Passport passport,
																		   @PathVariable Long serverId) {
		log.info("[Search] ChatMessageServiceController: 멤버별 서버 안읽은 메시지 수 조회 요청. memberId: {}, serverId: {}", passport.getMemberId(), serverId);
		return CommonResponse.onSuccess(historyService.getServerUnreadCount(passport.getMemberId(), serverId));
	}


	@Operation(summary = "멤버별 DM 안읽은 메시지 수 조회", description = "멤버 DM별로 안읽은 메시지 수 조회")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "멤버별 DM 안읽은 메시지 수 조회 성공", useReturnTypeSchema = true),
			@ApiResponse(responseCode = "400", description = "", content = @Content)
	})
	@GetMapping("/unread/dm")
	public CommonResponse<?> getMemberDmUnreadCount(@Parameter(hidden = true) @PassportUser Passport passport) {
		log.info("[Search] ChatMessageServiceController: 멤버별 DM 안읽은 메시지 수 조회 요청. memberId: {}", passport.getMemberId());
		return CommonResponse.onSuccess(null);
	}

	@PostMapping("/test/send")
	public CommonResponse<?> testSend(ChatMessageDto chatMessageDto) {
		chatMessageDto.setCreatedAt(LocalDateTime.now());
		historyService.saveChannelMessage(chatMessageDto);
		return CommonResponse.onSuccess(null);
	}

	@GetMapping("/test/unread/all/{memberId}")
	public CommonResponse<?> testUnreadAll(@PathVariable Long memberId) {
		return CommonResponse.onSuccess(historyService.getAllServerUnreadCount(memberId));
	}

}
