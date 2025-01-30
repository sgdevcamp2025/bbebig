package com.bbebig.serviceserver.server.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServerImageUpdateRequestDto {

    @Schema(description = "서버의 이미지", example = "https://...", required = true)
    private final String serverImageUrl;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ServerImageUpdateRequestDto(@JsonProperty("serverImageUrl") String serverImageUrl) {
        this.serverImageUrl = serverImageUrl;
    }
}
