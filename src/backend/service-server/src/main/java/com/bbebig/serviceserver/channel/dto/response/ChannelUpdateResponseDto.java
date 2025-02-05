package com.bbebig.serviceserver.channel.dto.response;

import com.bbebig.serviceserver.channel.entity.Channel;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChannelUpdateResponseDto {

    private final Long channelId;
    private final String channelName;
    private final boolean privateStatus;

    public static ChannelUpdateResponseDto convertToChannelUpdateResponseDto(Channel channel) {
        return ChannelUpdateResponseDto.builder()
                .channelId(channel.getId())
                .channelName(channel.getName())
                .privateStatus(channel.isPrivateStatus())
                .build();
    }
}
