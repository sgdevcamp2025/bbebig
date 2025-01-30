package com.bbebig.serviceserver.server.service;

import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import com.bbebig.serviceserver.server.dto.request.ServerCreateRequestDto;
import com.bbebig.serviceserver.server.dto.request.ServerImageUpdateRequestDto;
import com.bbebig.serviceserver.server.dto.request.ServerNameUpdateRequestDto;
import com.bbebig.serviceserver.server.dto.response.ServerCreateResponseDto;
import com.bbebig.serviceserver.server.dto.response.ServerDeleteResponseDto;
import com.bbebig.serviceserver.server.dto.response.ServerImageUpdateResponseDto;
import com.bbebig.serviceserver.server.dto.response.ServerNameUpdateResponseDto;
import com.bbebig.serviceserver.server.dto.response.ServerReadResponseDto;
import com.bbebig.serviceserver.server.entity.Server;
import com.bbebig.serviceserver.server.repository.ServerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ServerService {

    private final ServerRepository serverRepository;

    /**
     * 서버 생성
     */
    public ServerCreateResponseDto createServer(Long memberId, ServerCreateRequestDto serverCreateRequestDto) {
        Server server = Server.builder()
                .name(serverCreateRequestDto.getName())
                .ownerId(memberId)
                .serverImageUrl(serverCreateRequestDto.getServerImageUrl())
                .build();

        serverRepository.save(server);

        return ServerCreateResponseDto.convertToServerCreateResponseDto(server);
    }

    /**
     * 서버 정보 조회
     */
    public ServerReadResponseDto readServer(Long memberId, Long serverId) {
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.SERVER_NOT_FOUND));

        // TODO: memberId가 서버에 속한 인원인지 확인하는 로직 추가

        return ServerReadResponseDto.convertToServerReadResponseDto(server);
    }

    /**
     * 서버 이름 업데이트
     */
    public ServerNameUpdateResponseDto updateServerName(Long memberId, Long serverId, ServerNameUpdateRequestDto serverNameUpdateRequestDto) {
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.SERVER_NOT_FOUND));

        // 서버장 권한 체크
        checkServerOwner(memberId, server);

        server.updateName(serverNameUpdateRequestDto.getName());

        return ServerNameUpdateResponseDto.convertToServerNameUpdateResponseDto(server);
    }

    /**
     * 서버 이미지 업데이트
     */
    public ServerImageUpdateResponseDto updateServerImage(Long memberId, Long serverId, ServerImageUpdateRequestDto serverImageUpdateRequestDto) {
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.SERVER_NOT_FOUND));

        // 서버장 권한 체크
        checkServerOwner(memberId, server);

        server.updateServerImageUrl(serverImageUpdateRequestDto.getServerImageUrl());

        return ServerImageUpdateResponseDto.convertToServerImageUpdateResponseDto(server);
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
