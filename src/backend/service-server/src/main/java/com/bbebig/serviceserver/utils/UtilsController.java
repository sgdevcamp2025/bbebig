package com.bbebig.serviceserver.utils;

import com.bbebig.commonmodule.global.response.code.CommonResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("utils")
@RequiredArgsConstructor
@Tag(name = "유틸리티", description = "유틸리티 관련 API")
public class UtilsController {

    @Operation(summary = "헬스 체크", description = "헬스 체킹을 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "헬스 체크 성공", useReturnTypeSchema = true)
    })
    @GetMapping("/health")
    public CommonResponse<String> checkHealth() {
        log.info("[Service] 헬스 체크");
        return CommonResponse.onSuccess("hello, world!");
    }
}
