package com.bbebig.passportserver.passport.controller;

import com.bbebig.commonmodule.global.response.code.CommonResponse;
import com.bbebig.passportserver.passport.dto.PassportResponseDto;
import com.bbebig.passportserver.passport.service.PassportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("passports")
@RequiredArgsConstructor
@Tag(name = "서버", description = "서버 관련 API")
public class PassportController {

    private final PassportService passportService;

    @Operation(summary = "패스포트 생성", description = "패스포트를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "패스포트 생성 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @PostMapping("")
    public CommonResponse<PassportResponseDto> createServer(
            @RequestParam String jwt
    ) {
        log.info("[Passport] 패스포트 생성 요청");
        return CommonResponse.onSuccess(passportService.issuePassport(jwt));
    }

    @Operation(summary = "JWT 파싱", description = "JWT를 파싱합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "JWT 파싱 성공", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "400", description = "", content = @Content)
    })
    @GetMapping("")
    public CommonResponse<Long> parseJwt(
            @RequestParam String jwt
    ) {
        log.info("[Passport] JWT 파싱 요청");
        return CommonResponse.onSuccess(passportService.getMemberIdByJWT(jwt));
    }
}
