package com.bbebig.serviceserver.category.service;

import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import com.bbebig.serviceserver.category.dto.request.CategoryCreateRequestDto;
import com.bbebig.serviceserver.category.dto.request.CategoryUpdateRequestDto;
import com.bbebig.serviceserver.category.dto.response.CategoryCreateResponseDto;
import com.bbebig.serviceserver.category.dto.response.CategoryDeleteResponseDto;
import com.bbebig.serviceserver.category.dto.response.CategoryReadResponseDto;
import com.bbebig.serviceserver.category.dto.response.CategoryUpdateResponseDto;
import com.bbebig.serviceserver.category.entity.Category;
import com.bbebig.serviceserver.category.repository.CategoryRepository;
import com.bbebig.serviceserver.channel.entity.Channel;
import com.bbebig.serviceserver.channel.repository.ChannelRepository;
import com.bbebig.serviceserver.server.entity.Server;
import com.bbebig.serviceserver.server.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ServerRepository serverRepository;
    private final ChannelRepository channelRepository;

    /**
     * 카테고리 생성
     */
    public CategoryCreateResponseDto createCategory(Long memberId, CategoryCreateRequestDto categoryCreateRequestDto) {
        // 서버 조회
        Server server = serverRepository.findById(categoryCreateRequestDto.getServerId())
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.SERVER_NOT_FOUND));

        // 서버장 권한 체크
        checkServerOwner(memberId, server);

        // position 설정
        int position = calculateNextCategoryPosition(server);

        // 카테고리 생성
        Category category = Category.builder()
                .server(server)
                .name(categoryCreateRequestDto.getCategoryName())
                .position(position)
                .build();

        categoryRepository.save(category);

        return CategoryCreateResponseDto.convertToCategoryCreateResponseDto(category);
    }

    /**
     * 카테고리 정보 업데이트
     */
    public CategoryUpdateResponseDto updateCategory(Long memberId, Long categoryId, CategoryUpdateRequestDto categoryUpdateRequestDto) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.CATEGORY_NOT_FOUND));

        // 서버장 권한 체크
        checkServerOwner(memberId, category.getServer());

        category.update(categoryUpdateRequestDto);

        return CategoryUpdateResponseDto.convertToCategoryUpdateResponseDto(category);
    }

    /**
     * 카테고리 삭제
     */
    public CategoryDeleteResponseDto deleteCategory(Long memberId, Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.CATEGORY_NOT_FOUND));

        // 서버장 권한 체크
        checkServerOwner(memberId, category.getServer());

        // 카테고리에 속해있던 채널들 카테고리를 null로 업데이트
        List<Channel> channels = channelRepository.findAllByCategory(category);
        channels.forEach(channel -> {
            channel.updateCategoryNull();
            channel.updatePosition(calculateNextChannelPosition(channel.getServer()));
        });

        categoryRepository.delete(category);

        return CategoryDeleteResponseDto.convertToCategoryDeleteResponseDto(category);
    }

    /**
     * 카테고리 정보 조회
     */
    public CategoryReadResponseDto readCategory(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.CATEGORY_NOT_FOUND));

        return CategoryReadResponseDto.convertToCategoryReadResponseDto(category);
    }

    /**
     * 카테고리의 다음 position 계산
     */
    private int calculateNextCategoryPosition(Server server) {
        return categoryRepository.findMaxPositionByServerId(server.getId())
                .orElse(0) + 1;
    }

    /**
     * Category null인 Channel 다음 position 계산
     */
    private int calculateNextChannelPosition(Server server) {
        // 카테고리가 없는 채널 최대 위치 + 1
        return channelRepository.findMaxPositionByServerAndCategoryIsNull(server)
                .orElse(0) + 1;
    }

    /**
     * 서버장 권한 체크
     */
    private void checkServerOwner(Long memberId, Server server) {
        if (!server.getOwnerId().equals(memberId)) {
            throw new ErrorHandler(ErrorStatus.SERVER_OWNER_FORBIDDEN);
        }
    }
}
