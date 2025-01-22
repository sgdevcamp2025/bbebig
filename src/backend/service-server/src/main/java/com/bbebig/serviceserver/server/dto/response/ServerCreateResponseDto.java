package com.bbebig.serviceserver.server.dto.response;

import com.bbebig.serviceserver.server.entity.Server;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ServerCreateResponseDto {

    private final Long id;

    @JsonCreator(mode = Mode.PROPERTIES)
    public ServerCreateResponseDto(@JsonProperty("id") Long id) {
        this.id = id;
    }

    public static ServerCreateResponseDto convertToServerCreateResponseDto(Server server) {
        return ServerCreateResponseDto.builder()
                .id(server.getId())
                .build();
    }
}
