package com.bbebig.serviceserver.server.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServerNameUpdateRequestDto {

    @Schema(description = "사용자의 ID", example = "123", required = true)
    private final Long memberId;

    @Schema(description = "서버의 이름", example = "BBebig의 서버", required = true)
    private final String name;
}
