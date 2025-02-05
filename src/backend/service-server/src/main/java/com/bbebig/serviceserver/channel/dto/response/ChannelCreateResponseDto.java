package com.bbebig.serviceserver.channel.dto.response;

import com.bbebig.serviceserver.channel.entity.Channel;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChannelCreateResponseDto {

    private final Long id;

    @JsonCreator(mode = Mode.PROPERTIES)
    public ChannelCreateResponseDto(@JsonProperty("id") Long id) {
        this.id = id;
    }

    public static ChannelCreateResponseDto convertToChannelCreateResponseDto(Channel channel) {
        return ChannelCreateResponseDto.builder()
                .id(channel.getId())
                .build();
    }
}
