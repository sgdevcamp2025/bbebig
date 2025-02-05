package com.bbebig.serviceserver.category.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CategoryUpdateRequestDto {

    @Schema(description = "채널의 이름", example = "BBebig의 채널", required = true)
    private final String categoryName;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public CategoryUpdateRequestDto(@JsonProperty("categoryName") String categoryName) {
        this.categoryName = categoryName;
    }
}
