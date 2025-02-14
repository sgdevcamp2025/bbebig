package com.bbebig.serviceserver.server.dto.response;

import com.bbebig.serviceserver.category.entity.Category;
import com.bbebig.serviceserver.channel.entity.Channel;
import com.bbebig.serviceserver.channel.entity.ChannelMember;
import com.bbebig.serviceserver.server.entity.Server;
import com.bbebig.serviceserver.server.entity.ServerMember;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
@Builder
public class ServerReadResponseDto {

    private final Long serverId;
    private final String serverName;
    private final Long ownerId;
    private final String serverImageUrl;
    private final List<CategoryInfo> categoryInfoList;
    private final List<ServerMemberInfo> serverMemberInfoList;



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
        private final List<Long> channelMemberIdList;
    }

    @Data
    @Builder
    public static class ServerMemberInfo {
        private Long memberId;
        private String nickName;
        private String profileImageUrl;
        private LocalDateTime joinAt;
    }

    public static ServerReadResponseDto convertToServerReadResponseDto(Server server, List<Channel> channelList, List<Category> categoryList,
                                                                       List<ServerMember> serverMemberList, Map<Long, List<ChannelMember>> channelMemberMap) {
        List<CategoryInfo> categoryInfoList = new ArrayList<>();

        for (Category category : categoryList) {
            List<ChannelInfo> channelInfoList = new ArrayList<>();
            for (Channel channel : channelList) {
                if (channel.getCategory().getId().equals(category.getId())) {
                    ChannelInfo channelInfo = convertToChannelInfo(channel, channelMemberMap.get(channel.getId()).stream().map(channelMember -> channelMember.getServerMember().getMemberId()).toList());
                    channelInfoList.add(channelInfo);
                }
            }
            CategoryInfo categoryInfo = convertToCategoryInfo(category, channelInfoList);
            categoryInfoList.add(categoryInfo);
        }

        return ServerReadResponseDto.builder()
                .serverId(server.getId())
                .serverName(server.getName())
                .ownerId(server.getOwnerId())
                .serverImageUrl(server.getServerImageUrl())
                .categoryInfoList(categoryInfoList)
                .serverMemberInfoList(serverMemberList.stream().map(ServerReadResponseDto::convertToServerMemberInfo).toList())
                .build();
    }

    public static ChannelInfo convertToChannelInfo(Channel channel, List<Long> channelMemberIdList) {
        return ChannelInfo.builder()
                .channelId(channel.getId())
                .categoryId(channel.getCategory() == null ? null : channel.getCategory().getId())
                .channelName(channel.getName())
                .position(channel.getPosition())
                .channelType(channel.getChannelType().name())
                .channelMemberIdList(channelMemberIdList)
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


    public static ServerMemberInfo convertToServerMemberInfo(ServerMember serverMember) {
        return ServerMemberInfo.builder()
                .memberId(serverMember.getMemberId())
                .nickName(serverMember.getMemberNickname())
                .profileImageUrl(serverMember.getMemberProfileImageUrl())
                .joinAt(serverMember.getCreatedAt())
                .build();
    }

}
