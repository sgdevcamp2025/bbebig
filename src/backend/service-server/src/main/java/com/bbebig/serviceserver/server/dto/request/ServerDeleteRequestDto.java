package com.bbebig.serviceserver.server.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServerDeleteRequestDto {

    @Schema(description = "사용자의 ID", example = "123", required = true)
    private final Long memberId;

    @JsonCreator(mode = Mode.PROPERTIES)
    public ServerDeleteRequestDto(@JsonProperty("memberId") Long memberId) {
        this.memberId = memberId;
    }
 }
