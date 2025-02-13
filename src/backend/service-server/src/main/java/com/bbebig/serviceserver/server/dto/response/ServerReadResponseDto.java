package com.bbebig.serviceserver.server.dto.response;

import com.bbebig.serviceserver.channel.entity.Channel;
import com.bbebig.serviceserver.server.entity.Server;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class ServerReadResponseDto {

    private final Long serverId;
    private final String serverName;
    private final Long ownerId;
    private final String serverImageUrl;
    private final List<ChannelInfo> channelInfoList;

    @Data
    @Builder
    public static class ChannelInfo {
        private Long channelId;
        private Long categoryId;
        private int position;
        private String channelType;
        private boolean privateStatus;
    }

    public static ServerReadResponseDto convertToServerReadResponseDto(Server server, List<Channel> channelList) {
        return ServerReadResponseDto.builder()
                .serverId(server.getId())
                .serverName(server.getName())
                .ownerId(server.getOwnerId())
                .serverImageUrl(server.getServerImageUrl())
                .channelInfoList(channelList.stream()
                        .map(ServerReadResponseDto::convertToChannelInfo)
                        .toList())
                .build();
    }

    public static ChannelInfo convertToChannelInfo(Channel channel) {
        return ChannelInfo.builder()
                .channelId(channel.getId())
                .categoryId(channel.getCategory() == null ? null : channel.getCategory().getId())
                .position(channel.getPosition())
                .channelType(channel.getChannelType().name())
                .privateStatus(channel.isPrivateStatus())
                .build();
    }

}
