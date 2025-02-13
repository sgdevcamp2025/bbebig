package com.bbebig.serviceserver.server.dto.response;

import com.bbebig.serviceserver.server.entity.Server;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServerWithdrawResponseDto {

    private final Long serverId;

    @JsonCreator(mode = Mode.PROPERTIES)
    public ServerWithdrawResponseDto(@JsonProperty("serverId") Long serverId) {
        this.serverId = serverId;
    }

    public static ServerWithdrawResponseDto convertToServerWithdrawResponseDto(Server server) {
        return ServerWithdrawResponseDto.builder()
                .serverId(server.getId())
                .build();
    }
}
