package com.bbebig.serviceserver.server.service;

import com.bbebig.serviceserver.global.response.code.error.ErrorStatus;
import com.bbebig.serviceserver.global.response.exception.ErrorHandler;
import com.bbebig.serviceserver.server.dto.request.ServerCreateRequestDto;
import com.bbebig.serviceserver.server.dto.request.ServerDeleteRequestDto;
import com.bbebig.serviceserver.server.dto.request.ServerImageUpdateRequestDto;
import com.bbebig.serviceserver.server.dto.request.ServerNameUpdateRequestDto;
import com.bbebig.serviceserver.server.dto.request.ServerReadRequestDto;
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
    public ServerCreateResponseDto createServer(ServerCreateRequestDto serverCreateRequestDto) {
        Server server = Server.builder()
                .name(serverCreateRequestDto.getName())
                .ownerId(serverCreateRequestDto.getOwnerId())
                .serverImageUrl(serverCreateRequestDto.getServerImageUrl())
                .build();

        serverRepository.save(server);

        return ServerCreateResponseDto.convertToServerCreateResponseDto(server);
    }

    /**
     * 서버 정보 조회
     */
    public ServerReadResponseDto readServer(Long serverId, ServerReadRequestDto serverReadRequestDto) {
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.SERVER_NOT_FOUND));
        
        // TODO: memberId가 서버에 속한 인원인지 확인하는 로직 추가

        return ServerReadResponseDto.convertToServerReadResponseDto(server);
    }

    /**
     * 서버 이름 업데이트
     */
    public ServerNameUpdateResponseDto updateServerName(Long serverId, ServerNameUpdateRequestDto serverNameUpdateRequestDto) {
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.SERVER_NOT_FOUND));

        // 서버장 권한 체크
        checkServerOwner(serverNameUpdateRequestDto.getMemberId(), server);

        server.updateName(serverNameUpdateRequestDto.getName());

        return ServerNameUpdateResponseDto.convertToServerNameUpdateResponseDto(server);
    }

    /**
     * 서버 이미지 업데이트
     */
    public ServerImageUpdateResponseDto updateServerImage(Long serverId, ServerImageUpdateRequestDto serverImageUpdateRequestDto) {
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.SERVER_NOT_FOUND));

        // 서버장 권한 체크
        checkServerOwner(serverImageUpdateRequestDto.getMemberId(), server);

        server.updateServerImageUrl(serverImageUpdateRequestDto.getServerImageUrl());

        return ServerImageUpdateResponseDto.convertToServerImageUpdateResponseDto(server);
    }

    /**
     * 서버 삭제
     */
    public ServerDeleteResponseDto deleteServer(Long serverId, ServerDeleteRequestDto serverDeleteRequestDto) {
        Server server = serverRepository.findById(serverId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.SERVER_NOT_FOUND));

        // 서버장 권한 체크
        checkServerOwner(serverDeleteRequestDto.getMemberId(), server);

        // TODO: 삭제 조건 있는지 논의
        // TODO: ServerMember 삭제, Channel 삭제, ChannelMember 삭제, Category 삭제

        serverRepository.delete(server);

        return ServerDeleteResponseDto.convertToServerDeleteResponseDto(server);
    }

    // 서버장 권한 체크
    private static void checkServerOwner(Long memberId, Server server) {
        if (!server.getOwnerId().equals(memberId)) {
            throw new ErrorHandler(ErrorStatus.SERVER_OWNER_FORBIDDEN);
        }
    }

}
