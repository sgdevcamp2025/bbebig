package com.bbebig.serviceserver.server.dto.response;

import com.bbebig.serviceserver.server.entity.Server;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServerImageUpdateResponseDto {

    private final Long id;
    private final String serverImageUrl;

    public static ServerImageUpdateResponseDto convertToServerImageUpdateResponseDto(Server server) {
        return ServerImageUpdateResponseDto.builder()
                .id(server.getId())
                .serverImageUrl(server.getServerImageUrl())
                .build();
    }
}
