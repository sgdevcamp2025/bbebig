package com.bbebig.serviceserver.category.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryCreateRequestDto {

    @Schema(description = "서버의 ID", example = "1", required = true)
    private final Long serverId;

    @Schema(description = "카테고리의 이름", example = "BBeBig의 카테고리", required = true)
    private final String categoryName;
}
