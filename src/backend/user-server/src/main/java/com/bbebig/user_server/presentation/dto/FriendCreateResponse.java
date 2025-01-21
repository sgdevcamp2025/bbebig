package com.bbebig.user_server.presentation.dto;

import com.bbebig.user_server.domain.Friend;
import com.bbebig.user_server.domain.FriendStatus;
import com.bbebig.user_server.domain.Member;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public record FriendCreateResponse(
        Long id,
        Member fromMember,
        LocalDateTime createdAt) {
    public static FriendCreateResponse of(Friend friend) {
        return new FriendCreateResponse(friend.getId(), friend.getFromMember(), friend.getCreatedAt());
    }
}
