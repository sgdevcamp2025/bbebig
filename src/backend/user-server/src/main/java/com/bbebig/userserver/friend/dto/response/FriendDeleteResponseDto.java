package com.bbebig.userserver.friend.dto.response;

import com.bbebig.userserver.friend.entity.Friend;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FriendDeleteResponseDto {

    private final Long id;

    @JsonCreator(mode = Mode.PROPERTIES)
    public FriendDeleteResponseDto(@JsonProperty("id") Long id) {
        this.id = id;
    }

    public static FriendDeleteResponseDto convertToFriendDeleteResponseDto(Friend friend) {
        return FriendDeleteResponseDto.builder()
                .id(friend.getId())
                .build();
    }
}
