package com.bbebig.userserver.friend.dto.response;

import com.bbebig.userserver.friend.entity.Friend;
import com.bbebig.userserver.member.entity.Member;

import java.time.LocalDateTime;

public record FriendResponse(
        Long id,
        Member friendMember,
        String serverList,
        LocalDateTime createdAt
) {
    public static FriendResponse of(Friend friend) {
        return new FriendResponse(friend.getId(), friend.getFromMember(),friend.getServerList(), friend.getCreatedAt());
    }
}
