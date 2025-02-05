package com.bbebig.serviceserver.server.dto.response;

import com.bbebig.serviceserver.server.entity.Server;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServerNameUpdateResponseDto {

    private final Long id;
    private final String name;

    public static ServerNameUpdateResponseDto convertToServerNameUpdateResponseDto(Server server) {
        return ServerNameUpdateResponseDto.builder()
                .id(server.getId())
                .name(server.getName())
                .build();
    }
}
