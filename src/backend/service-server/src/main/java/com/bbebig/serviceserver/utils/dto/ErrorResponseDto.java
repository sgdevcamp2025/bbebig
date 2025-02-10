package com.bbebig.serviceserver.utils.dto;

import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.serviceserver.server.entity.Server;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ErrorResponseDto {

    private final Integer statusCode;
    private final HttpStatus httpStatus;
    private final String customErrorCode;
    private final String message;

    public static ErrorResponseDto convertToErrorResponseDto(ErrorStatus errorStatus) {
        return ErrorResponseDto.builder()
                .statusCode(errorStatus.getHttpStatus().value())
                .httpStatus(errorStatus.getHttpStatus())
                .customErrorCode(errorStatus.getCode())
                .message(errorStatus.getMessage())
                .build();
    }
}
