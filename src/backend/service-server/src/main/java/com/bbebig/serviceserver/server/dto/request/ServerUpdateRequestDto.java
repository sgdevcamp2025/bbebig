package com.bbebig.serviceserver.server.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServerUpdateRequestDto {

    @Schema(description = "서버의 이름", example = "BBebig의 서버", required = true)
    private final String serverName;

    @Schema(description = "서버의 이미지", example = "https://... | null", required = true)
    private final String serverImageUrl;
}
