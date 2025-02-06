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

    private final Long channelId;

    @JsonCreator(mode = Mode.PROPERTIES)
    public ChannelCreateResponseDto(@JsonProperty("channelId") Long channelId) {
        this.channelId = channelId;
    }

    public static ChannelCreateResponseDto convertToChannelCreateResponseDto(Channel channel) {
        return ChannelCreateResponseDto.builder()
                .channelId(channel.getId())
                .build();
    }
}
