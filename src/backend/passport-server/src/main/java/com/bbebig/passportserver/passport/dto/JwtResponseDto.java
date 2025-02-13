package com.bbebig.passportserver.passport.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JwtResponseDto {

    private final Long memberId;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public JwtResponseDto(@JsonProperty("memberId") Long memberId) {
        this.memberId = memberId;
    }

    public static JwtResponseDto convertToJwtResponseDto(Long memberId) {
        return JwtResponseDto.builder()
                .memberId(memberId)
                .build();
    }
}
