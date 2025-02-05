package com.bbebig.serviceserver.server.dto.response;

import com.bbebig.serviceserver.server.entity.Server;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ServerListReadResponseDto {

    private final List<ServerListEntryReadResponseDto> servers;

    public static ServerListReadResponseDto convertToServerListReadResponseDto(List<Server> servers) {
        return ServerListReadResponseDto.builder()
                .servers(servers.stream()
                        .map(ServerListEntryReadResponseDto::convertToServerListEntryReadResponseDto)
                        .toList())
                .build();
    }

    @Getter
    @Builder
    private static class ServerListEntryReadResponseDto {
        private final Long serverId;
        private final String serverName;
        private final String serverImageUrl;

        private static ServerListEntryReadResponseDto convertToServerListEntryReadResponseDto(Server server) {
            return ServerListEntryReadResponseDto.builder()
                    .serverId(server.getId())
                    .serverName(server.getName())
                    .serverImageUrl(server.getServerImageUrl())
                    .build();
        }
    }
}
