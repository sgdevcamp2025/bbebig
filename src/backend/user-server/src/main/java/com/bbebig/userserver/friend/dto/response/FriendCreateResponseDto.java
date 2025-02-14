package com.bbebig.userserver.friend.dto.response;

import com.bbebig.userserver.friend.entity.Friend;
import com.bbebig.userserver.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class FriendCreateResponseDto {

    private final Long friendId;
    private final Long fromMemberId;
    private final Long toMemberId;
    private final LocalDateTime createdAt;

    public static FriendCreateResponseDto convertToFriendCreateResponseDto(Friend friend) {
        return FriendCreateResponseDto.builder()
                .friendId(friend.getId())
                .fromMemberId(friend.getFromMember().getId())
                .toMemberId(friend.getToMember().getId())
                .createdAt(friend.getCreatedAt())
                .build();
    }
}
