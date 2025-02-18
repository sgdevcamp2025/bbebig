package com.bbebig.serviceserver.category.service;

import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import com.bbebig.commonmodule.kafka.dto.model.ChannelType;
import com.bbebig.commonmodule.kafka.dto.serverEvent.ServerCategoryEventDto;
import com.bbebig.commonmodule.kafka.dto.serverEvent.ServerChannelEventDto;
import com.bbebig.commonmodule.kafka.dto.serverEvent.ServerEventType;
import com.bbebig.commonmodule.kafka.dto.serverEvent.status.ServerCategoryStatus;
import com.bbebig.commonmodule.kafka.dto.serverEvent.status.ServerChannelStatus;
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
import com.bbebig.serviceserver.global.kafka.KafkaProducerService;
import com.bbebig.serviceserver.server.entity.Server;
import com.bbebig.serviceserver.server.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final ServerRepository serverRepository;
    private final ChannelRepository channelRepository;

    private final KafkaProducerService kafkaProducerService;

    /**
     * 카테고리 생성
     */
    @Transactional
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

        // 카테고리 생성 이벤트 발행
        ServerCategoryEventDto serverCategoryEventDto = ServerCategoryEventDto.builder()
                .serverId(server.getId())
                .type(ServerEventType.SERVER_CATEGORY)
                .categoryId(category.getId())
                .categoryName(category.getName())
                .order(category.getPosition())
                .status(ServerCategoryStatus.CREATE)
                .build();

        kafkaProducerService.sendServerEvent(serverCategoryEventDto);

        return CategoryCreateResponseDto.convertToCategoryCreateResponseDto(category);
    }

    /**
     * 카테고리 정보 업데이트
     */
    @Transactional
    public CategoryUpdateResponseDto updateCategory(Long memberId, Long categoryId, CategoryUpdateRequestDto categoryUpdateRequestDto) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.CATEGORY_NOT_FOUND));

        // 서버장 권한 체크
        checkServerOwner(memberId, category.getServer());

        category.update(categoryUpdateRequestDto);

        // Kafka 이벤트 발행
        Server server = category.getServer();
        ServerCategoryEventDto serverCategoryEventDto = ServerCategoryEventDto.builder()
                .serverId(server.getId())
                .type(ServerEventType.SERVER_CATEGORY)
                .categoryId(category.getId())
                .categoryName(category.getName())
                .order(category.getPosition())
                .status(ServerCategoryStatus.UPDATE)
                .build();
        kafkaProducerService.sendServerEvent(serverCategoryEventDto);

        return CategoryUpdateResponseDto.convertToCategoryUpdateResponseDto(category);
    }

    /**
     * 카테고리 삭제
     */
    @Transactional
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

        // Kafka 이벤트 발행
        Server server = category.getServer();
        ServerCategoryEventDto serverCategoryEventDto = ServerCategoryEventDto.builder()
                .serverId(server.getId())
                .type(ServerEventType.SERVER_CATEGORY)
                .categoryId(category.getId())
                .status(ServerCategoryStatus.DELETE)
                .build();
        kafkaProducerService.sendServerEvent(serverCategoryEventDto);

        for (Channel channel : channels) {
            ServerChannelEventDto serverChannelEventDto = ServerChannelEventDto.builder()
                    .serverId(server.getId())
                    .type(ServerEventType.SERVER_CHANNEL)
                    .categoryId(null)
                    .channelId(channel.getId())
                    .channelName(channel.getName())
                    .channelType(ChannelType.valueOf(channel.getChannelType().toString()))
                    .status(ServerChannelStatus.UPDATE)
                    .build();
        }

        return CategoryDeleteResponseDto.convertToCategoryDeleteResponseDto(category);
    }

    /**
     * 카테고리 정보 조회
     */
    @Transactional(readOnly = true)
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
