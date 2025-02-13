package com.bbebig.serviceserver.channel.controller;

import com.bbebig.commonmodule.clientDto.serviceServer.CommonServiceServerClientResponseDto;
import com.bbebig.commonmodule.clientDto.serviceServer.CommonServiceServerClientResponseDto.ServerLastInfoResponseDto;
import com.bbebig.commonmodule.global.response.code.CommonResponse;
import com.bbebig.commonmodule.passport.annotation.PassportUser;
import com.bbebig.commonmodule.proto.PassportProto.Passport;
import com.bbebig.commonmodule.redis.domain.ChannelLastInfo;
import com.bbebig.serviceserver.channel.dto.request.ChannelCreateRequestDto;
import com.bbebig.serviceserver.channel.dto.request.ChannelUpdateRequestDto;
import com.bbebig.serviceserver.channel.dto.response.ChannelCreateResponseDto;
import com.bbebig.serviceserver.channel.dto.response.ChannelDeleteResponseDto;
import com.bbebig.serviceserver.channel.dto.response.ChannelReadResponseDto;
import com.bbebig.serviceserver.channel.dto.response.ChannelUpdateResponseDto;
import com.bbebig.serviceserver.channel.service.ChannelService;
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
@RequestMapping("channels")
@RequiredArgsConstructor
@Tag(name = "채널", description = "채널 관련 API")
public class ChannelController {

    private final ChannelService channelService;

    @Operation(summary = "채널 생성 (서버장만 가능)", description = "채널을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채널 생성 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @PostMapping("")
    public CommonResponse<ChannelCreateResponseDto> createChannel(
            @Parameter(hidden = true) @PassportUser Passport passport,
            @RequestBody ChannelCreateRequestDto channelCreateRequestDto
    ) {
        log.info("[Service] 채널 생성 요청: memberId = {}, serverId = {}", passport.getMemberId(), channelCreateRequestDto.getServerId());
        return CommonResponse.onSuccess(channelService.createChannel(passport.getMemberId(), channelCreateRequestDto));
    }

    @Operation(summary = "채널 업데이트 (서버장만 가능)", description = "채널을 업데이트합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채널 업데이트 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @PutMapping("/{channelId}")
    public CommonResponse<ChannelUpdateResponseDto> updateChannel(
            @Parameter(hidden = true) @PassportUser Passport passport,
            @PathVariable Long channelId,
            @RequestBody ChannelUpdateRequestDto channelUpdateRequestDto
    ) {
        log.info("[Service] 채널 업데이트 요청: memberId = {}, channelId = {}", passport.getMemberId(), channelId);
        return CommonResponse.onSuccess(channelService.updateChannel(passport.getMemberId(), channelId, channelUpdateRequestDto));
    }

    @Operation(summary = "채널 삭제 (서버장만 가능)", description = "채널을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채널 삭제 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @DeleteMapping("/{channelId}")
    public CommonResponse<ChannelDeleteResponseDto> deleteChannel(
            @Parameter(hidden = true) @PassportUser Passport passport,
            @PathVariable Long channelId
    ) {
        log.info("[Service] 채널 삭제 요청: memberId = {}, channelId = {}", passport.getMemberId(), channelId);
        return CommonResponse.onSuccess(channelService.deleteChannel(passport.getMemberId(), channelId));
    }

    @Operation(summary = "채널 정보 조회", description = "채널 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채널 정보 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @GetMapping("/{channelId}")
    public CommonResponse<ChannelReadResponseDto> readChannel(
            @Parameter(hidden = true) @PassportUser Passport passport,
            @PathVariable Long channelId
    ) {
        log.info("[Service] 채널 정보 조회 요청: memberId = {}, channelId = {}", passport.getMemberId(), channelId);
        return CommonResponse.onSuccess(channelService.readChannel(channelId));
    }

    @Operation(summary = "채널 마지막 방문 정보 조회", description = "채널 마지막 방문 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "채널 마지막 방문 정보 조회 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @GetMapping("/{channelId}/lastInfo/member/{memberId}")
    public CommonResponse<ChannelLastInfo> getChannelLastInfo(@PathVariable Long channelId, @PathVariable Long memberId) {
        log.info("[Service] 채널 마지막 방문 정보 조회 요청: channelId = {}, memberId = {}", channelId, memberId);
        return CommonResponse.onSuccess(channelService.getChannelLastInfo(channelId, memberId));
    }
}
