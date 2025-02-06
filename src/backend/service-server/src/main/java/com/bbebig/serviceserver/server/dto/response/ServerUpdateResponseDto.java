package com.bbebig.serviceserver.server.dto.response;

import com.bbebig.serviceserver.server.entity.Server;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServerUpdateResponseDto {

    private final Long serverId;
    private final String serverName;
    private final String serverImageUrl;

    public static ServerUpdateResponseDto convertToServerUpdateResponseDto(Server server) {
        return ServerUpdateResponseDto.builder()
                .serverId(server.getId())
                .serverName(server.getName())
                .serverImageUrl(server.getServerImageUrl())
                .build();
    }
}
