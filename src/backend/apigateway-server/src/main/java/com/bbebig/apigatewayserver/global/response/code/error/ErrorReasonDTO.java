package com.bbebig.apigatewayserver.global.response.code.error;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ErrorReasonDTO<T> {

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
