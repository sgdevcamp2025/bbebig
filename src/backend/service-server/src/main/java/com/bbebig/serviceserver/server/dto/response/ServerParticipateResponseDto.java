package com.bbebig.serviceserver.server.dto.response;

import com.bbebig.serviceserver.server.entity.Server;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServerParticipateResponseDto {

    private final Long serverId;

    @JsonCreator(mode = Mode.PROPERTIES)
    public ServerParticipateResponseDto(@JsonProperty("serverId") Long serverId) {
        this.serverId = serverId;
    }

    public static ServerParticipateResponseDto convertToServerParticipateResponseDto(Server server) {
        return ServerParticipateResponseDto.builder()
                .serverId(server.getId())
                .build();
    }
}
