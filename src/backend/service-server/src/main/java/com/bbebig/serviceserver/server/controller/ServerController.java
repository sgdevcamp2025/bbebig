package com.bbebig.serviceserver.server.controller;

import com.bbebig.serviceserver.global.response.code.CommonResponse;
import com.bbebig.serviceserver.server.dto.request.ServerCreateRequestDto;
import com.bbebig.serviceserver.server.dto.request.ServerDeleteRequestDto;
import com.bbebig.serviceserver.server.dto.request.ServerImageUpdateRequestDto;
import com.bbebig.serviceserver.server.dto.request.ServerNameUpdateRequestDto;
import com.bbebig.serviceserver.server.dto.request.ServerReadRequestDto;
import com.bbebig.serviceserver.server.dto.response.ServerCreateResponseDto;
import com.bbebig.serviceserver.server.dto.response.ServerDeleteResponseDto;
import com.bbebig.serviceserver.server.dto.response.ServerImageUpdateResponseDto;
import com.bbebig.serviceserver.server.dto.response.ServerNameUpdateResponseDto;
import com.bbebig.serviceserver.server.dto.response.ServerReadResponseDto;
import com.bbebig.serviceserver.server.service.ServerService;
import io.swagger.v3.oas.annotations.Operation;
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
            @RequestBody ServerCreateRequestDto serverCreateRequestDto
    ) {
        log.info("[Service] 서버 생성 요청: ownerId = {}", serverCreateRequestDto.getOwnerId());
        return CommonResponse.onSuccess(serverService.createServer(serverCreateRequestDto));
    }

    @Operation(summary = "서버 조회", description = "서버를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "서버 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @GetMapping("/{serverId}")
    public CommonResponse<ServerReadResponseDto> readServer(
            @PathVariable Long serverId,
            @RequestBody ServerReadRequestDto serverReadRequestDto
    ) {
        log.info("[Service] 서버 조회 요청: memberId = {}, serverId = {}",
                serverReadRequestDto.getMemberId(), serverId);
        return CommonResponse.onSuccess(serverService.readServer(serverId, serverReadRequestDto));
    }

    @Operation(summary = "서버 이름 업데이트 (서버장만 가능)", description = "서버 이름을 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "서버 이름 업데이트 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @PatchMapping("/{serverId}/name")
    public CommonResponse<ServerNameUpdateResponseDto> updateServerName(
            @PathVariable Long serverId,
            @RequestBody ServerNameUpdateRequestDto serverNameUpdateRequestDto
    ) {
        log.info("[Service] 서버 이름 업데이트 요청: memberId = {}, serverId = {}",
                serverNameUpdateRequestDto.getMemberId(), serverId);
        return CommonResponse.onSuccess(serverService.updateServerName(serverId, serverNameUpdateRequestDto));
    }

    @Operation(summary = "서버 이미지 업데이트 (서버장만 가능)", description = "서버 이미지를 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "서버 이미지 업데이트 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @PatchMapping("/{serverId}/image")
    public CommonResponse<ServerImageUpdateResponseDto> updateServerImage(
            @PathVariable Long serverId,
            @RequestBody ServerImageUpdateRequestDto serverImageUpdateRequestDto
    ) {
        log.info("[Service] 서버 이미지 업데이트 요청: memberId = {}, serverId = {}",
                serverImageUpdateRequestDto.getMemberId(), serverId);
        return CommonResponse.onSuccess(serverService.updateServerImage(serverId, serverImageUpdateRequestDto));
    }

    @Operation(summary = "서버 삭제 (서버장만 가능)", description = "서버를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "서버 삭제 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @DeleteMapping("/{serverId}")
    public CommonResponse<ServerDeleteResponseDto> deleteServer(
            @PathVariable Long serverId,
            @RequestBody ServerDeleteRequestDto serverDeleteRequestDto
    ) {
        log.info("[Service] 서버 삭제 요청: memberId = {}, serverId = {}",
                serverDeleteRequestDto.getMemberId(), serverId);
        return CommonResponse.onSuccess(serverService.deleteServer(serverId, serverDeleteRequestDto));
    }
}
