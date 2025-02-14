package com.bbebig.serviceserver.category.dto.response;

import com.bbebig.serviceserver.category.entity.Category;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryCreateResponseDto {

    private final Long categoryId;

    @JsonCreator(mode = Mode.PROPERTIES)
    public CategoryCreateResponseDto(@JsonProperty("categoryId") Long categoryId) {
        this.categoryId = categoryId;
    }

    public static CategoryCreateResponseDto convertToCategoryCreateResponseDto(Category category) {
        return CategoryCreateResponseDto.builder()
                .categoryId(category.getId())
                .build();
    }
}
