package com.bbebig.userserver.member.controller;

import com.bbebig.commonmodule.clientDto.userServer.CommonUserServerResponseDto;
import com.bbebig.commonmodule.clientDto.userServer.CommonUserServerResponseDto.MemberGlobalStatusResponseDto;
import com.bbebig.commonmodule.clientDto.userServer.CommonUserServerResponseDto.MemberInfoResponseDto;
import com.bbebig.commonmodule.global.response.code.CommonResponse;
import com.bbebig.commonmodule.passport.annotation.PassportUser;
import com.bbebig.commonmodule.proto.PassportProto.Passport;
import com.bbebig.userserver.member.dto.request.MemberPresenceUpdateRequestDto;
import com.bbebig.userserver.member.dto.request.MemberUpdateRequestDto;
import com.bbebig.userserver.member.dto.response.MemberDeleteResponseDto;
import com.bbebig.userserver.member.dto.response.MemberPresenceUpdateResponseDto;
import com.bbebig.userserver.member.dto.response.MemberReadResponseDto;
import com.bbebig.userserver.member.dto.response.MemberUpdateResponseDto;
import com.bbebig.userserver.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/members")
@RequiredArgsConstructor
@Tag(name = "멤버", description = "멤버 관련 API")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "멤버 정보 업데이트", description = "멤버 정보를 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "멤버 정보 업데이트 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @PatchMapping("")
    public CommonResponse<MemberUpdateResponseDto> updateMember(
            @Parameter(hidden = true) @PassportUser Passport passport,
            @RequestBody MemberUpdateRequestDto memberUpdateRequestDto
    ) {
        log.info("[Member] 멤버 정보 업데이트 요청: memberId = {}", passport.getMemberId());
        return CommonResponse.onSuccess(memberService.updateMember(passport.getMemberId(), memberUpdateRequestDto));
    }

    @Operation(summary = "멤버 접속 정보 업데이트", description = "멤버 접속 정보를 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "멤버 접속 정보 업데이트 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @PatchMapping("/presence")
    public CommonResponse<MemberPresenceUpdateResponseDto> updateMemberPresence(
            @Parameter(hidden = true) @PassportUser Passport passport,
            @RequestBody MemberPresenceUpdateRequestDto memberPresenceUpdateRequestDto
    ) {
        log.info("[Member] 멤버 접속 정보 업데이트 요청: memberId = {}", passport.getMemberId());
        return CommonResponse.onSuccess(memberService.updateMemberPresence(passport.getMemberId(), memberPresenceUpdateRequestDto));
    }

    @Operation(summary = "멤버 탈퇴 (삭제)", description = "멤버를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "멤버 삭제 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @DeleteMapping("")
    public CommonResponse<MemberDeleteResponseDto> deleteMember(
            @Parameter(hidden = true) @PassportUser Passport passport
    ) {
        log.info("[Member] 멤버 탈퇴 (삭제) 요청: memberId = {}", passport.getMemberId());
        return CommonResponse.onSuccess(memberService.deleteMember(passport.getMemberId()));
    }

    @Operation(summary = "멤버 본인 정보 조회", description = "멤버 본인 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "멤버 본인 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @GetMapping("/self")
    public CommonResponse<MemberReadResponseDto> readMemberSelf(
            @Parameter(hidden = true) @PassportUser Passport passport
    ) {
        log.info("[Member] 멤버 본인 조회 요청: memberId = {}", passport.getMemberId());
        return CommonResponse.onSuccess(memberService.readMember(passport.getMemberId()));
    }

    @Operation(summary = "멤버 정보 조회", description = "멤버 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "멤버 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @GetMapping("/{memberId}")
    public CommonResponse<MemberReadResponseDto> readMember(
            @PathVariable Long memberId
    ) {
        log.info("[Member] 특정 멤버 조회 요청: memberId = {}", memberId);
        return CommonResponse.onSuccess(memberService.readMember(memberId));
    }

    @Operation(summary = "멤버 정보 조회 (For Feign Client)", description = "멤버 정보를 조회 (For Feign Client)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "멤버 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @GetMapping("/feign/{memberId}")
    public CommonResponse<MemberInfoResponseDto> getMemberInfo(
            @PathVariable Long memberId
    ) {
        log.info("[Member] 멤버 정보 조회 요청: memberId = {}", memberId);
        return CommonResponse.onSuccess(memberService.getMemberInfo(memberId));
    }

    @Operation(summary = "멤버 전역 상태 조회", description = "멤버 전역 상태를 조회합니다. (For Feign Client)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "멤버 전역 상태 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @GetMapping("/{memberId}/global-status")
    public CommonResponse<MemberGlobalStatusResponseDto> getMemberGlobalStatus(
            @PathVariable Long memberId
    ) {
        log.info("[Member] 멤버 전역 상태 조회 요청: memberId = {}", memberId);
        return CommonResponse.onSuccess(memberService.getMemberGlobalStatus(memberId));
    }


}
