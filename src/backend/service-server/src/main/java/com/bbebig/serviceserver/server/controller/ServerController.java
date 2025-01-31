package com.bbebig.serviceserver.server.controller;

import com.bbebig.commonmodule.global.response.code.CommonResponse;
import com.bbebig.commonmodule.passport.annotation.PassportUser;
import com.bbebig.commonmodule.proto.PassportProto.Passport;
import com.bbebig.serviceserver.server.dto.request.*;
import com.bbebig.serviceserver.server.dto.response.ServerCreateResponseDto;
import com.bbebig.serviceserver.server.dto.response.ServerDeleteResponseDto;
import com.bbebig.serviceserver.server.dto.response.ServerImageUpdateResponseDto;
import com.bbebig.serviceserver.server.dto.response.ServerNameUpdateResponseDto;
import com.bbebig.serviceserver.server.dto.response.ServerReadResponseDto;
import com.bbebig.serviceserver.server.service.ServerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("servers")
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

    @Operation(summary = "서버 조회", description = "서버를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "서버 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @GetMapping("/{serverId}")
    public CommonResponse<ServerReadResponseDto> readServer(
            @Parameter(hidden = true) @PassportUser Passport passport,
            @PathVariable Long serverId
    ) {
        log.info("[Service] 서버 조회 요청: memberId = {}, serverId = {}", passport.getMemberId(), serverId);
        return CommonResponse.onSuccess(serverService.readServer(passport.getMemberId(), serverId));
    }

    @Operation(summary = "서버 이름 업데이트 (서버장만 가능)", description = "서버 이름을 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "서버 이름 업데이트 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @PatchMapping("/{serverId}/name")
    public CommonResponse<ServerNameUpdateResponseDto> updateServerName(
            @Parameter(hidden = true) @PassportUser Passport passport,
            @PathVariable Long serverId,
            @RequestBody ServerNameUpdateRequestDto serverNameUpdateRequestDto
    ) {
        log.info("[Service] 서버 이름 업데이트 요청: memberId = {}, serverId = {}", passport.getMemberId(), serverId);
        return CommonResponse.onSuccess(serverService.updateServerName(passport.getMemberId(), serverId, serverNameUpdateRequestDto));
    }

    @Operation(summary = "서버 이미지 업데이트 (서버장만 가능)", description = "서버 이미지를 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "서버 이미지 업데이트 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @PatchMapping("/{serverId}/image")
    public CommonResponse<ServerImageUpdateResponseDto> updateServerImage(
            @Parameter(hidden = true) @PassportUser Passport passport,
            @PathVariable Long serverId,
            @RequestBody ServerImageUpdateRequestDto serverImageUpdateRequestDto
    ) {
        log.info("[Service] 서버 이미지 업데이트 요청: memberId = {}, serverId = {}", passport.getMemberId(), serverId);
        return CommonResponse.onSuccess(serverService.updateServerImage(passport.getMemberId(), serverId, serverImageUpdateRequestDto));
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
        return CommonResponse.onSuccess(serverService.deleteServer(serverId, serverId));
    }
}
