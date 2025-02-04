package com.bbebig.serviceserver.server.service;

import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import com.bbebig.serviceserver.category.entity.Category;
import com.bbebig.serviceserver.category.repository.CategoryRepository;
import com.bbebig.serviceserver.channel.entity.Channel;
import com.bbebig.serviceserver.channel.entity.ChannelMember;
import com.bbebig.serviceserver.channel.entity.ChannelType;
import com.bbebig.serviceserver.channel.repository.ChannelMemberRepository;
import com.bbebig.serviceserver.channel.repository.ChannelRepository;
import com.bbebig.serviceserver.server.dto.request.ServerCreateRequestDto;
import com.bbebig.serviceserver.server.dto.request.ServerUpdateRequestDto;
import com.bbebig.serviceserver.server.dto.response.*;
import com.bbebig.serviceserver.server.entity.RoleType;
import com.bbebig.serviceserver.server.entity.Server;
import com.bbebig.serviceserver.server.entity.ServerMember;
import com.bbebig.serviceserver.server.repository.ServerMemberRepository;
import com.bbebig.serviceserver.server.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ServerService {

    private final ServerRepository serverRepository;
    private final ServerMemberRepository serverMemberRepository;
    private final CategoryRepository categoryRepository;
    private final ChannelRepository channelRepository;
    private final ChannelMemberRepository channelMemberRepository;

    /**
     * 서버 생성
     */
    public ServerCreateResponseDto createServer(Long memberId, ServerCreateRequestDto serverCreateRequestDto) {
        Server server = Server.builder()
                .name(serverCreateRequestDto.getServerName())
                .ownerId(memberId)
                .serverImageUrl(serverCreateRequestDto.getServerImageUrl())
                .build();

        // TODO: 마일스톤2 에서 Passport 에 member 정보 넣기
        ServerMember serverMember = ServerMember.builder()
                .server(server)
                .memberId(memberId)
                .memberNickname(null)
                .memberProfileImageUrl(null)
                .roleType(RoleType.OWNER)
                .build();

        Category chatCategory = Category.builder()
                .server(server)
                .name("채팅 채널")
                .position(1)
                .build();

        Channel chatChannel = Channel.builder()
                .server(server)
                .category(chatCategory)
                .name("일반")
                .position(1)
                .channelType(ChannelType.CHAT)
                .privateStatus(false)
                .build();

        ChannelMember chatChannelMember = ChannelMember.builder()
                .channel(chatChannel)
                .serverMember(serverMember)
                .build();

        Category streamCategory = Category.builder()
                .server(server)
                .name("음성 채널")
                .position(2)
                .build();

        Channel streamChannel = Channel.builder()
                .server(server)
                .category(streamCategory)
                .name("일반")
                .position(1)
                .channelType(ChannelType.STREAM)
                .privateStatus(false)
                .build();

        ChannelMember streamChannelMember = ChannelMember.builder()
                .channel(streamChannel)
                .serverMember(serverMember)
                .build();

        serverRepository.save(server);
        serverMemberRepository.save(serverMember);
        categoryRepository.save(chatCategory);
        categoryRepository.save(streamCategory);
        channelRepository.save(chatChannel);
        channelMemberRepository.save(chatChannelMember);
        channelRepository.save(streamChannel);
        channelMemberRepository.save(streamChannelMember);

        return ServerCreateResponseDto.convertToServerCreateResponseDto(server);
    }

    /**
     * 서버 정보 조회
     */
    public ServerReadResponseDto readServer(Long memberId, Long serverId) {
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.SERVER_NOT_FOUND));

        // 서버에 속한 멤버인지 확인
        if (!serverMemberRepository.existsByServerIdAndMemberId(serverId, memberId)) {
            throw new ErrorHandler(ErrorStatus.SERVER_MEMBER_FORBIDDEN);
        }

        return ServerReadResponseDto.convertToServerReadResponseDto(server);
    }

    /**
     * 서버 목록 조회
     */
    public ServerListReadResponseDto readServerList(Long memberId) {
        List<ServerMember> serverMembers = serverMemberRepository.findAllByMemberId(memberId);

        List<Server> servers = serverMembers.stream()
                .map(ServerMember::getServer)
                .toList();

        return ServerListReadResponseDto.convertToServerListReadResponseDto(servers);
    }

    /**
     * 서버 업데이트
     */
    public ServerUpdateResponseDto updateServerName(Long memberId, Long serverId, ServerUpdateRequestDto serverUpdateRequestDto) {
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.SERVER_NOT_FOUND));

        // 서버장 권한 체크
        checkServerOwner(memberId, server);

        server.update(serverUpdateRequestDto);

        return ServerUpdateResponseDto.convertToServerUpdateResponseDto(server);
    }

    /**
     * 서버 삭제
     */
    public ServerDeleteResponseDto deleteServer(Long memberId, Long serverId) {
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.SERVER_NOT_FOUND));

        // 서버장 권한 체크
        checkServerOwner(memberId, server);

        // TODO: 삭제 조건 있는지 논의
        // TODO: ServerMember 삭제, Channel 삭제, ChannelMember 삭제, Category 삭제

        serverRepository.delete(server);

        return ServerDeleteResponseDto.convertToServerDeleteResponseDto(server);
    }

    // 서버장 권한 체크
    private void checkServerOwner(Long memberId, Server server) {
        if (!server.getOwnerId().equals(memberId)) {
            throw new ErrorHandler(ErrorStatus.SERVER_OWNER_FORBIDDEN);
        }
    }
}
