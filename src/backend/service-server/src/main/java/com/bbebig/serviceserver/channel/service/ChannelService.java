package com.bbebig.serviceserver.channel.service;

import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import com.bbebig.commonmodule.kafka.dto.model.ChannelType;
import com.bbebig.commonmodule.kafka.dto.serverEvent.ServerChannelEventDto;
import com.bbebig.commonmodule.kafka.dto.serverEvent.ServerEventType;
import com.bbebig.commonmodule.kafka.dto.serverEvent.status.ServerChannelStatus;
import com.bbebig.commonmodule.redis.domain.ChannelLastInfo;
import com.bbebig.serviceserver.category.entity.Category;
import com.bbebig.serviceserver.category.repository.CategoryRepository;
import com.bbebig.serviceserver.channel.dto.request.ChannelCreateRequestDto;
import com.bbebig.serviceserver.channel.dto.request.ChannelUpdateRequestDto;
import com.bbebig.serviceserver.channel.dto.response.ChannelCreateResponseDto;
import com.bbebig.serviceserver.channel.dto.response.ChannelDeleteResponseDto;
import com.bbebig.serviceserver.channel.dto.response.ChannelReadResponseDto;
import com.bbebig.serviceserver.channel.dto.response.ChannelUpdateResponseDto;
import com.bbebig.serviceserver.channel.entity.Channel;
import com.bbebig.serviceserver.channel.entity.ChannelMember;
import com.bbebig.serviceserver.channel.repository.ChannelMemberRepository;
import com.bbebig.serviceserver.channel.repository.ChannelRepository;
import com.bbebig.serviceserver.global.kafka.KafkaProducerService;
import com.bbebig.serviceserver.server.entity.Server;
import com.bbebig.serviceserver.server.entity.ServerMember;
import com.bbebig.serviceserver.server.repository.ServerMemberRepository;
import com.bbebig.serviceserver.server.repository.ServerRedisRepositoryImpl;
import com.bbebig.serviceserver.server.repository.ServerRepository;
import com.bbebig.serviceserver.server.service.ServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChannelService {

    private final ChannelRepository channelRepository;
    private final ChannelMemberRepository channelMemberRepository;
    private final ServerRepository serverRepository;
    private final ServerMemberRepository serverMemberRepository;
    private final CategoryRepository categoryRepository;

    private final ServerRedisRepositoryImpl serverRedisRepository;
    private final ServerService serverService;
    private final KafkaProducerService kafkaProducerService;

    /**
     * 채널 생성
     */
    @Transactional
    public ChannelCreateResponseDto createChannel(Long memberId, ChannelCreateRequestDto channelCreateRequestDto) {
        // 서버 조회
        Server server = serverRepository.findById(channelCreateRequestDto.getServerId())
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.SERVER_NOT_FOUND));

        // 서버장 권한 체크
        checkServerOwner(memberId, server);

        // 카테고리 조회 (없으면 null)
        Category category = categoryRepository.findById(channelCreateRequestDto.getCategoryId())
                .orElse(null);

        // position 설정
        int position = calculateNextPosition(server, category);

        // 채널 생성
        Channel channel = Channel.builder()
                .server(server)
                .category(category)
                .name(channelCreateRequestDto.getChannelName())
                .position(position)
                .channelType(channelCreateRequestDto.getChannelType())
                .privateStatus(channelCreateRequestDto.isPrivateStatus())
                .lastSequence(1L)
                .build();

        channelRepository.save(channel);

        // 채널 멤버 설정
        if (channelCreateRequestDto.isPrivateStatus()) {
            // 비공개 채널이면 특정 멤버만 추가
            addPrivateChannelMembers(channel, channelCreateRequestDto.getMemberIds(), server);
        } else {
            // 공개 채널이면 서버 멤버 전체 추가
            addPublicChannelMembers(channel, server);
        }

        // 채널 생성 시 Redis에 채널 정보 캐싱
        if (serverRedisRepository.existsServerChannelList(server.getId())) {
            serverService.makeServerChannelListCache(server.getId());
        } else {
            serverRedisRepository.addServerChannelToSet(server.getId(), channel.getId());
        }

        // Kafka로 데이터 발행
        ServerChannelEventDto serverChannelEventDto = ServerChannelEventDto.builder()
                .serverId(server.getId())
                .type(ServerEventType.SERVER_CHANNEL)
                .categoryId(category != null ? category.getId() : null)
                .channelId(channel.getId())
                .channelName(channel.getName())
                .channelType(ChannelType.valueOf(channel.getChannelType().toString()))
                .order(channel.getPosition())
                .status(ServerChannelStatus.CREATE)
                .build();
        kafkaProducerService.sendServerEvent(serverChannelEventDto);

        return ChannelCreateResponseDto.convertToChannelCreateResponseDto(channel);
    }

    /**
     * 채널 정보 업데이트
     */
    @Transactional
    public ChannelUpdateResponseDto updateChannel(Long memberId, Long channelId, ChannelUpdateRequestDto channelUpdateRequestDto) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.CHANNEL_NOT_FOUND));

        // 서버장 권한 체크
        checkServerOwner(memberId, channel.getServer());

        channel.update(channelUpdateRequestDto);

        if (channelUpdateRequestDto.isPrivateStatus()) {
            // 비공개 채널이면 특정 멤버만 추가
            addPrivateChannelMembers(channel, channelUpdateRequestDto.getMemberIds(), channel.getServer());
        } else {
            // 공개 채널이면 서버 멤버 전체 추가
            addPublicChannelMembers(channel, channel.getServer());
        }

        // Kafka로 데이터 발행
        Server server = channel.getServer();
        ServerChannelEventDto serverChannelEventDto = ServerChannelEventDto.builder()
                .serverId(server.getId())
                .type(ServerEventType.SERVER_CHANNEL)
                .categoryId(channel.getCategory() != null ? channel.getCategory().getId() : null)
                .channelId(channel.getId())
                .channelName(channel.getName())
                .channelType(ChannelType.valueOf(channel.getChannelType().toString()))
                .order(channel.getPosition())
                .status(ServerChannelStatus.UPDATE)
                .build();
        kafkaProducerService.sendServerEvent(serverChannelEventDto);

        return ChannelUpdateResponseDto.convertToChannelUpdateResponseDto(channel);
    }

    /**
     * 채널 삭제
     */
    @Transactional
    public ChannelDeleteResponseDto deleteChannel(Long memberId, Long channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.CHANNEL_NOT_FOUND));

        // 서버장 권한 체크
        checkServerOwner(memberId, channel.getServer());

        channelMemberRepository.deleteAllByChannel(channel);
        channelRepository.delete(channel);

        // 채널 삭제 시 Redis에서 채널 정보 삭제
        serverRedisRepository.removeServerChannelFromSet(channel.getServer().getId(), channel.getId());

        // Kafka로 데이터 발행
        Server server = channel.getServer();
        ServerChannelEventDto serverChannelEventDto = ServerChannelEventDto.builder()
                .serverId(server.getId())
                .type(ServerEventType.SERVER_CHANNEL)
                .categoryId(channel.getCategory() != null ? channel.getCategory().getId() : null)
                .channelId(channel.getId())
                .channelType(ChannelType.valueOf(channel.getChannelType().toString()))
                .status(ServerChannelStatus.DELETE)
                .build();
        kafkaProducerService.sendServerEvent(serverChannelEventDto);

        return ChannelDeleteResponseDto.convertToChannelDeleteResponseDto(channel);
    }

    /**
     * 채널 정보 조회
     */
    @Transactional(readOnly = true)
    public ChannelReadResponseDto readChannel(Long channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.CHANNEL_NOT_FOUND));

        return ChannelReadResponseDto.convertToChannelReadResponseDto(channel);
    }

    public ChannelLastInfo getChannelLastInfo(Long channelId, Long memberId) {
        ChannelMember channelMember = channelMemberRepository.findByServerMemberIdAndChannelId(memberId, channelId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.CHANNEL_MEMBER_NOT_FOUND));

        return ChannelLastInfo.builder()
                .channelId(channelId)
                .lastReadMessageId(channelMember.getLastReadMessageId())
                .lastAccessAt(channelMember.getLastAccessAt())
                .lastReadSequence(channelMember.getLastReadSequence())
                .build();
    }

    /**
     * 다음 position 계산
     */
    private int calculateNextPosition(Server server, Category category) {
        if (category != null) {
            // 특정 카테고리에 속한 채널 최대 위치 + 1
            return channelRepository.findMaxPositionByServerAndCategory(server, category)
                    .orElse(0) + 1;
        } else {
            // 카테고리가 없는 채널 최대 위치 + 1
            return channelRepository.findMaxPositionByServerAndCategoryIsNull(server)
                    .orElse(0) + 1;
        }
    }

    /**
     * 비공개 채널 - 특정 멤버만 추가
     */
    private void addPrivateChannelMembers(Channel channel, List<Long> memberIds, Server server) {
        List<ServerMember> serverMembers = serverMemberRepository.findByServerAndMemberIdIn(server, memberIds);

        List<ChannelMember> channelMembers = serverMembers.stream()
                .map(serverMember -> ChannelMember.builder()
                        .channel(channel)
                        .serverMember(serverMember)
                        .build())
                .collect(Collectors.toList());

        channelMemberRepository.saveAll(channelMembers);
    }

    /**
     * 공개 채널 - 서버의 모든 멤버 추가
     */
    private void addPublicChannelMembers(Channel channel, Server server) {
        List<ServerMember> serverMembers = serverMemberRepository.findByServer(server);

        List<ChannelMember> channelMembers = serverMembers.stream()
                .map(serverMember -> ChannelMember.builder()
                        .channel(channel)
                        .serverMember(serverMember)
                        .build())
                .collect(Collectors.toList());

        channelMemberRepository.saveAll(channelMembers);
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
