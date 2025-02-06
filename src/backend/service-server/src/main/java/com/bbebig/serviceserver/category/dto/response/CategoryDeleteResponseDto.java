package com.bbebig.serviceserver.category.dto.response;

import com.bbebig.serviceserver.category.entity.Category;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryDeleteResponseDto {

    private final Long categoryId;

    @JsonCreator(mode = Mode.PROPERTIES)
    public CategoryDeleteResponseDto(@JsonProperty("categoryId") Long categoryId) {
        this.categoryId = categoryId;
    }

    public static CategoryDeleteResponseDto convertToCategoryDeleteResponseDto(Category category) {
        return CategoryDeleteResponseDto.builder()
                .categoryId(category.getId())
                .build();
    }
}
