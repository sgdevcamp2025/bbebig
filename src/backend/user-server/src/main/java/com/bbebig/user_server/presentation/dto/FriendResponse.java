package com.bbebig.user_server.presentation.dto;

import com.bbebig.user_server.domain.Friend;
import com.bbebig.user_server.domain.Member;

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
