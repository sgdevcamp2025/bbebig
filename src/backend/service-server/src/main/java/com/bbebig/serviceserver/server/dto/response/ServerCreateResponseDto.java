package com.bbebig.serviceserver.server.dto.response;

import com.bbebig.serviceserver.server.entity.Server;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServerCreateResponseDto {

    private final Long serverId;

    @JsonCreator(mode = Mode.PROPERTIES)
    public ServerCreateResponseDto(@JsonProperty("serverId") Long serverId) {
        this.serverId = serverId;
    }

    public static ServerCreateResponseDto convertToServerCreateResponseDto(Server server) {
        return ServerCreateResponseDto.builder()
                .serverId(server.getId())
                .build();
    }
}
