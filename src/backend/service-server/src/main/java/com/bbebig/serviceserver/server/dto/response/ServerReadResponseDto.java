package com.bbebig.serviceserver.server.dto.response;

import com.bbebig.serviceserver.server.entity.Server;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServerReadResponseDto {

    private final Long serverId;
    private final String serverName;
    private final Long ownerId;
    private final String serverImageUrl;

    public static ServerReadResponseDto convertToServerReadResponseDto(Server server) {
        return ServerReadResponseDto.builder()
                .serverId(server.getId())
                .serverName(server.getName())
                .ownerId(server.getOwnerId())
                .serverImageUrl(server.getServerImageUrl())
                .build();
    }
}
