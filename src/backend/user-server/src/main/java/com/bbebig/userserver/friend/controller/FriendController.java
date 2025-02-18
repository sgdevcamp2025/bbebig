package com.bbebig.userserver.friend.controller;

import com.bbebig.commonmodule.global.response.code.CommonResponse;
import com.bbebig.commonmodule.passport.annotation.PassportUser;
import com.bbebig.commonmodule.proto.PassportProto.Passport;
import com.bbebig.userserver.friend.dto.request.FriendCreateRequestDto;
import com.bbebig.userserver.friend.dto.response.*;
import com.bbebig.userserver.friend.dto.response.FriendResponseDto.FriendListResponseDto;
import com.bbebig.userserver.friend.dto.response.FriendResponseDto.PendingFriendListResponseDto;
import com.bbebig.userserver.friend.service.FriendService;
import com.bbebig.userserver.friend.entity.FriendStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("friends")
@RequiredArgsConstructor
@Tag(name = "친구", description = "친구 관련 API")
public class FriendController {

    private final FriendService friendService;

    @Operation(summary = "친구 요청 (생성)", description = "친구를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "친구 생성 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @PostMapping("")
    public CommonResponse<FriendCreateResponseDto> createFriend(
            @Parameter(hidden = true) @PassportUser Passport passport,
            @RequestBody FriendCreateRequestDto friendCreateRequestDto
    ) {
        log.info("[Member] 친구 생성 요청: memberId = {}", passport.getMemberId());
        return CommonResponse.onSuccess(friendService.createFriend(passport.getMemberId(), friendCreateRequestDto));
    }

    @Operation(summary = "요청 대기 중인 친구 조회", description = "요청 대기 중인 친구 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 대기 중인 친구 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @GetMapping("/pending")
    public CommonResponse<PendingFriendListResponseDto> getPendingFriends(
            @Parameter(hidden = true) @PassportUser Passport passport
    ) {
        log.info("[Member] 요청 대기 중인 친구 조회 요청: memberId = {}", passport.getMemberId());
        return CommonResponse.onSuccess(friendService.getPendingFriends(passport.getMemberId()));
    }

    @Operation(summary = "친구 목록 조회", description = "전체 친구 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "요청 수락한 친구 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @GetMapping
    public CommonResponse<FriendListResponseDto> getAcceptedFriends(
            @Parameter(hidden = true) @PassportUser Passport passport
    ) {
        log.info("[Member] 요청 수락한 친구 조회 요청: memberId = {}", passport.getMemberId());
        return CommonResponse.onSuccess(friendService.getAcceptedFriends(passport.getMemberId()));
    }

    @Operation(summary = "친구 삭제", description = "친구를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "친구 삭제 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @DeleteMapping("/{friendId}")
    public CommonResponse<FriendDeleteResponseDto> deleteFriend(
            @Parameter(hidden = true) @PassportUser Passport passport,
            @PathVariable Long friendId
    ) {
        log.info("[Member] 친구 삭제 요청: memberId = {}", passport.getMemberId());
        return CommonResponse.onSuccess(friendService.deleteFriend(passport.getMemberId(), friendId));
    }

    @Operation(summary = "친구 요청 수락", description = "친구 요청을 수락합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "친구 요청 수락 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @PatchMapping("/{friendId}/accepted")
    public CommonResponse<FriendUpdateResponseDto> updateFriendStatusAccepted(
            @Parameter(hidden = true) @PassportUser Passport passport,
            @PathVariable Long friendId
    ) {
        log.info("[Member] 친구 요청 수락 요청: memberId = {}", passport.getMemberId());
        return CommonResponse.onSuccess(friendService.updateFriendStatus(passport.getMemberId(), friendId, FriendStatus.ACCEPTED));
    }

    @Operation(summary = "친구 요청 거절", description = "친구 요청을 거절합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "친구 요청 거절 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @PatchMapping("/{friendId}/declined")
    public CommonResponse<FriendUpdateResponseDto> updateFriendStatusDeclined(
            @Parameter(hidden = true) @PassportUser Passport passport,
            @PathVariable Long friendId
    ) {
        log.info("[Member] 친구 요청 거절 요청: memberId = {}", passport.getMemberId());
        return CommonResponse.onSuccess(friendService.updateFriendStatus(passport.getMemberId(), friendId, FriendStatus.DECLINED));
    }

    @Operation(summary = "친구 요청 취소", description = "보낸 친구 요청을 취소합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "친구 요청 취소 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @DeleteMapping("/{friendId}/cancel")
    public CommonResponse<FriendDeleteResponseDto> cancelFriendPending(
            @Parameter(hidden = true) @PassportUser Passport passport,
            @PathVariable Long friendId
    ) {
        log.info("[Member] 친구 요청 취소 요청: memberId = {}", passport.getMemberId());
        return CommonResponse.onSuccess(friendService.cancelFriendPending(passport.getMemberId(), friendId));
    }
}
