package com.bbebig.commondomain.response.code.success;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ReasonDTO {
    private HttpStatus httpStatus;

    private final String code;
    private final String message;
}
