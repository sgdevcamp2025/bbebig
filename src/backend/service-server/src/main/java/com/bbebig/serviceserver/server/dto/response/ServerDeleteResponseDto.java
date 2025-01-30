package com.bbebig.serviceserver.server.dto.response;

import com.bbebig.serviceserver.server.entity.Server;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServerDeleteResponseDto {

    private final Long id;

    @JsonCreator(mode = Mode.PROPERTIES)
    public ServerDeleteResponseDto(@JsonProperty("id") Long id) {
        this.id = id;
    }

    public static ServerDeleteResponseDto convertToServerDeleteResponseDto(Server server) {
        return ServerDeleteResponseDto.builder()
                .id(server.getId())
                .build();
    }
}
