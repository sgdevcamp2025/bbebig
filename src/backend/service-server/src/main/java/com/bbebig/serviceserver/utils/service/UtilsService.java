package com.bbebig.serviceserver.utils.service;

import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.serviceserver.utils.dto.ErrorResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class UtilsService {

    public ErrorResponseDto readErrorCode(String errorCode) {
        return Arrays.stream(ErrorStatus.values())
                .filter(error -> error.getCode().equals(errorCode))
                .findFirst()
                .map(ErrorResponseDto::convertToErrorResponseDto)
                .orElse(ErrorResponseDto.builder()
                        .customErrorCode("UNKNOWN_ERROR")
                        .message("해당 에러 코드를 찾을 수 없습니다.")
                        .build());
    }
}
