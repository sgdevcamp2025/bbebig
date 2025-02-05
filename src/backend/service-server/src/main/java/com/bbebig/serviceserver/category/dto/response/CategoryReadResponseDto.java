package com.bbebig.serviceserver.category.dto.response;

import com.bbebig.serviceserver.category.entity.Category;
import com.bbebig.serviceserver.channel.entity.Channel;
import com.bbebig.serviceserver.channel.entity.ChannelType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryReadResponseDto {

    private final Long categoryId;
    private final Long serverId;
    private final String categoryName;
    private final int categoryPosition;

    public static CategoryReadResponseDto convertToCategoryReadResponseDto(Category category) {
        return CategoryReadResponseDto.builder()
                .categoryId(category.getId())
                .serverId(category.getServer().getId())
                .categoryName(category.getName())
                .categoryPosition(category.getPosition())
                .build();
    }
}
