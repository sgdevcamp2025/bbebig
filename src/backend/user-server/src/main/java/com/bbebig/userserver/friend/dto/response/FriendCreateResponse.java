package com.bbebig.userserver.friend.dto.response;

import com.bbebig.userserver.friend.entity.Friend;
import com.bbebig.userserver.member.entity.Member;

import java.time.LocalDateTime;

public record FriendCreateResponse(
        Long id,
        Member fromMember,
        LocalDateTime createdAt) {
    public static FriendCreateResponse of(Friend friend) {
        return new FriendCreateResponse(friend.getId(), friend.getFromMember(), friend.getCreatedAt());
    }
}
