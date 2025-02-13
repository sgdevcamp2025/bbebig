package com.bbebig.userserver.friend.dto.response;

import com.bbebig.userserver.friend.entity.Friend;
import com.bbebig.userserver.friend.entity.FriendStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FriendUpdateResponseDto {

    private final Long id;
    private final FriendStatus friendStatus;

    public static FriendUpdateResponseDto convertToFriendUpdateResponseDto(Friend friend) {
        return FriendUpdateResponseDto.builder()
                .id(friend.getId())
                .friendStatus(friend.getFriendStatus())
                .build();
    }
}
