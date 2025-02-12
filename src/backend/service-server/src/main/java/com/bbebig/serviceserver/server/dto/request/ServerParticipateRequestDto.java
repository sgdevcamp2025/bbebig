package com.bbebig.serviceserver.server.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServerParticipateRequestDto {

    @Schema(description = "멤버의 닉네임", example = "이소은", required = true)
    private final String memberNickname;

    @Schema(description = "멤버의 이미지 URL", example = "https://...|null", required = true)
    private final String memberProfileUrl;
}
