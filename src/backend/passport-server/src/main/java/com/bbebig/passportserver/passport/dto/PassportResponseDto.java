package com.bbebig.passportserver.passport.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PassportResponseDto {

    private final String passport;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public PassportResponseDto(@JsonProperty("passport") String passport) {
        this.passport = passport;
    }

    public static PassportResponseDto convertToIssuePassportResponse(String passport) {
        return PassportResponseDto.builder()
                .passport(passport)
                .build();
    }
}
