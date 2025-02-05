package com.bbebig.serviceserver.category.dto.response;

import com.bbebig.serviceserver.category.entity.Category;
import com.bbebig.serviceserver.channel.entity.Channel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryUpdateResponseDto {

    private final Long categoryId;
    private final String categoryName;

    public static CategoryUpdateResponseDto convertToCategoryUpdateResponseDto(Category category) {
        return CategoryUpdateResponseDto.builder()
                .categoryId(category.getId())
                .categoryName(category.getName())
                .build();
    }
}
