package com.bbebig.serviceserver.server.dto.response;

import com.bbebig.serviceserver.category.entity.Category;
import com.bbebig.serviceserver.channel.entity.Channel;
import com.bbebig.serviceserver.server.entity.Server;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class ServerReadResponseDto {

    private final Long serverId;
    private final String serverName;
    private final Long ownerId;
    private final String serverImageUrl;
    private final List<CategoryInfo> categoryInfoList;

    @Data
    @Builder
    public static class CategoryInfo {
        private Long categoryId;
        private String categoryName;
        private int position;
        private List<ChannelInfo> channelInfoList;
    }

    @Data
    @Builder
    public static class ChannelInfo {
        private Long channelId;
        private Long categoryId;
        private String channelName;
        private int position;
        private String channelType;
        private boolean privateStatus;
    }

    public static ServerReadResponseDto convertToServerReadResponseDto(Server server, List<Channel> channelList, List<Category> categoryList) {
        List<CategoryInfo> categoryInfoList = new ArrayList<>();

        for (Category category : categoryList) {
            CategoryInfo categoryInfo = convertToCategoryInfo(category, channelList.stream()
                    .filter(channel -> channel.getCategory() != null && channel.getCategory().getId().equals(category.getId()))
                    .map(ServerReadResponseDto::convertToChannelInfo)
                    .toList());
            categoryInfoList.add(categoryInfo);
        }

        return ServerReadResponseDto.builder()
                .serverId(server.getId())
                .serverName(server.getName())
                .ownerId(server.getOwnerId())
                .serverImageUrl(server.getServerImageUrl())
                .categoryInfoList(categoryInfoList)
                .build();
    }

    public static ChannelInfo convertToChannelInfo(Channel channel) {
        return ChannelInfo.builder()
                .channelId(channel.getId())
                .categoryId(channel.getCategory() == null ? null : channel.getCategory().getId())
                .channelName(channel.getName())
                .position(channel.getPosition())
                .channelType(channel.getChannelType().name())
                .privateStatus(channel.isPrivateStatus())
                .build();
    }

    public static CategoryInfo convertToCategoryInfo(Category category, List<ChannelInfo> channelInfoList) {
        return CategoryInfo.builder()
                .categoryId(category.getId())
                .categoryName(category.getName())
                .position(category.getPosition())
                .channelInfoList(channelInfoList)
                .build();
    }

}
