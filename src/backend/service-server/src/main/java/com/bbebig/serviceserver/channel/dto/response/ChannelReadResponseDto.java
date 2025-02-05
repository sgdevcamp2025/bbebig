package com.bbebig.serviceserver.channel.dto.response;

import com.bbebig.serviceserver.channel.entity.Channel;
import com.bbebig.serviceserver.channel.entity.ChannelType;
import com.bbebig.serviceserver.server.entity.Server;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChannelReadResponseDto {

    private final Long channelId;
    private final String channelName;
    private final int channelPosition;
    private final ChannelType channelType;
    private final boolean privateStatus;

    public static ChannelReadResponseDto convertToChannelReadResponseDto(Channel channel) {
        return ChannelReadResponseDto.builder()
                .channelId(channel.getId())
                .channelName(channel.getName())
                .channelPosition(channel.getPosition())
                .channelType(channel.getChannelType())
                .privateStatus(channel.isPrivateStatus())
                .build();
    }
}
