package com.bbebig.serviceserver.server.controller;

import com.bbebig.commonmodule.global.response.code.CommonResponse;
import com.bbebig.commonmodule.passport.annotation.PassportUser;
import com.bbebig.commonmodule.proto.PassportProto.Passport;
import com.bbebig.serviceserver.server.dto.request.*;
import com.bbebig.serviceserver.server.dto.response.*;
import com.bbebig.serviceserver.server.dto.response.ServerReadResponseDto.ServerMemberInfoResponseDto;
import com.bbebig.serviceserver.server.service.ServerService;
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
@RequestMapping("/servers")
@RequiredArgsConstructor
@Tag(name = "서버", description = "서버 관련 API")
public class ServerController {

    private final ServerService serverService;

    @Operation(summary = "서버 생성", description = "서버를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "서버 생성 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @PostMapping("")
    public CommonResponse<ServerCreateResponseDto> createServer(
            @Parameter(hidden = true) @PassportUser Passport passport,
            @RequestBody ServerCreateRequestDto serverCreateRequestDto
    ) {
        log.info("[Service] 서버 생성 요청: ownerId = {}", passport.getMemberId());
        return CommonResponse.onSuccess(serverService.createServer(passport.getMemberId(), serverCreateRequestDto));
    }

    @Operation(summary = "서버 정보 조회", description = "서버 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "서버 정보 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @GetMapping("/{serverId}")
    public CommonResponse<ServerReadResponseDto> readServer(
            @Parameter(hidden = true) @PassportUser Passport passport,
            @PathVariable Long serverId
    ) {
        log.info("[Service] 서버 정보 조회 요청: memberId = {}, serverId = {}", passport.getMemberId(), serverId);
        return CommonResponse.onSuccess(serverService.readServer(serverId));
    }

    @Operation(summary = "서버 정보 조회", description = "서버 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "서버 정보 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @GetMapping("/{serverId}/members/info")
    public CommonResponse<ServerMemberInfoResponseDto> getServerMemberInfo(
            @Parameter(hidden = true) @PassportUser Passport passport,
            @PathVariable Long serverId
    ) {
        log.info("[Service] 서버 멤버 정보 조회 요청: memberId = {}, serverId = {}", passport.getMemberId(), serverId);
        return CommonResponse.onSuccess(serverService.getMemberInfoList(serverId));
    }



    @Operation(summary = "멤버별로 속해있는 서버 목록 조회 (For Client)", description = "멤버별로 속해있는 서버 목록 조회합니다. (For Client)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "멤버별로 속해있는 서버 목록 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @GetMapping()
    public CommonResponse<ServerListReadResponseDto> readServerListByPathVariable(
            @Parameter(hidden = true) @PassportUser Passport passport
    ) {
        log.info("[Service] 멤버별로 속해있는 서버 목록 조회 요청 (For Client): memberId = {}", passport.getMemberId());
        return CommonResponse.onSuccess(serverService.readServerList(passport.getMemberId()));
    }

    @Operation(summary = "서버 업데이트 (서버장만 가능)", description = "서버를 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "서버 업데이트 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @PutMapping("/{serverId}")
    public CommonResponse<ServerUpdateResponseDto> updateServer(
            @Parameter(hidden = true) @PassportUser Passport passport,
            @PathVariable Long serverId,
            @RequestBody ServerUpdateRequestDto serverUpdateRequestDto
    ) {
        log.info("[Service] 서버 업데이트 요청: memberId = {}, serverId = {}", passport.getMemberId(), serverId);
        return CommonResponse.onSuccess(serverService.updateServer(passport.getMemberId(), serverId, serverUpdateRequestDto));
    }

    @Operation(summary = "서버 삭제 (서버장만 가능)", description = "서버를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "서버 삭제 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @DeleteMapping("/{serverId}")
    public CommonResponse<ServerDeleteResponseDto> deleteServer(
            @Parameter(hidden = true) @PassportUser Passport passport,
            @PathVariable Long serverId
    ) {
        log.info("[Service] 서버 삭제 요청: memberId = {}, serverId = {}", passport.getMemberId(), serverId);
        return CommonResponse.onSuccess(serverService.deleteServer(passport.getMemberId(), serverId));
    }

    @GetMapping("/{serverId}/list/members")
    public CommonResponse<ServerMemberInfoResponseDto> getServerMemberInfo(
            @PathVariable Long serverId
    ) {
        log.info("[Service] 서버에 속해있는 멤버 목록 조회 요청: serverId = {}", serverId);
        return CommonResponse.onSuccess(serverService.getServerMemberInfo(serverId));
    }

    @Operation(summary = "서버 참여", description = "서버를 참여합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "서버 참여 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @PostMapping("/{serverId}/participate")
    public CommonResponse<ServerParticipateResponseDto> participateServer(
            @Parameter(hidden = true) @PassportUser Passport passport,
            @PathVariable Long serverId
    ) {
        log.info("[Service] 서버 참여 요청: serverId = {}, memberId = {}", serverId, passport.getMemberId());
        return CommonResponse.onSuccess(serverService.participateServer(passport.getMemberId(), serverId));
    }

    @Operation(summary = "서버 탈퇴", description = "서버를 탈퇴합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "서버 탈퇴 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @DeleteMapping("/{serverId}/withdraw")
    public CommonResponse<ServerWithdrawResponseDto> withdrawServer(
            @Parameter(hidden = true) @PassportUser Passport passport,
            @PathVariable Long serverId
    ) {
        log.info("[Service] 서버 탈퇴 요청: serverId = {}, memberId = {}", serverId, passport.getMemberId());
        return CommonResponse.onSuccess(serverService.withdrawServer(passport.getMemberId(), serverId));
    }
}
