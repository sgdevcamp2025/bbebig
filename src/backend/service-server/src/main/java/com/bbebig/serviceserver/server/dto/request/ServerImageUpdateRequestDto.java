package com.bbebig.serviceserver.server.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServerImageUpdateRequestDto {

    @Schema(description = "사용자의 ID", example = "123", required = true)
    private final Long memberId;

    @Schema(description = "서버의 이미지", example = "https://...", required = true)
    private final String serverImageUrl;
}
