package com.bbebig.serviceserver.server.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServerNameUpdateRequestDto {

    @Schema(description = "서버의 이름", example = "BBebig의 서버", required = true)
    private final String name;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ServerNameUpdateRequestDto(@JsonProperty("name") String name) {
        this.name = name;
    }
}
