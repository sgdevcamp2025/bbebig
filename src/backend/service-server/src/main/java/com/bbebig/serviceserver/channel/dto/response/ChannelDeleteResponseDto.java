package com.bbebig.serviceserver.channel.dto.response;

import com.bbebig.serviceserver.channel.entity.Channel;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChannelDeleteResponseDto {

    private final Long channelId;

    @JsonCreator(mode = Mode.PROPERTIES)
    public ChannelDeleteResponseDto(@JsonProperty("channelId") Long channelId) {
        this.channelId = channelId;
    }

    public static ChannelDeleteResponseDto convertToChannelDeleteResponseDto(Channel channel) {
        return ChannelDeleteResponseDto.builder()
                .channelId(channel.getId())
                .build();
    }
}
