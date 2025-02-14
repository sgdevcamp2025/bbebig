package com.bbebig.apigatewayserver.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PassportResponseDto {

    private String code;
    private String message;
    private Result result;

    @Getter
    public static class Result {
        private String passport;
    }
}
