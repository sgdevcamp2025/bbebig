package com.bbebig.serviceserver.server.controller;

import com.bbebig.commonmodule.global.response.code.CommonResponse;
import com.bbebig.serviceserver.server.dto.request.ServerParticipateRequestDto;
import com.bbebig.serviceserver.server.dto.response.PassportTestResponse;
import com.bbebig.serviceserver.server.dto.response.ServerParticipateResponseDto;
import com.bbebig.serviceserver.server.service.PassportTestClient;
import com.bbebig.serviceserver.server.service.ServerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/servers")
@RequiredArgsConstructor
@Tag(name = "서버", description = "서버 관련 API")
public class ServerTestController {

    private final ServerService serverService;
    private final PassportTestClient passportTestClient;

    @Operation(summary = "서버 참여 (서버 테스트 용)", description = "서버를 참여합니다. (서버 테스트 용)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "서버 참여 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @PostMapping("/{serverId}/participate/test")
    public CommonResponse<ServerParticipateResponseDto> participateServerTest(
            @RequestHeader("Authorization") String authorizationHeader,
            @PathVariable Long serverId
    ) {
        String jwt = extractJwtFromHeader(authorizationHeader);
        log.info("[Service] JWT로 Passport 서버 호출: {}", jwt);

        PassportTestResponse passportResponse = passportTestClient.getMemberIdByJwt(jwt);
        Long memberId = passportResponse.getResult().longValue();

        log.info("[Service] Passport 서버 응답: memberId = {}", memberId);
        return CommonResponse.onSuccess(serverService.participateServer(memberId, serverId));
    }

    private String extractJwtFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        throw new IllegalArgumentException("JWT 토큰이 없습니다.");
    }
}
