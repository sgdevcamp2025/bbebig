package com.bbebig.userserver.friend.dto.response;

import com.bbebig.userserver.friend.entity.Friend;
import com.bbebig.userserver.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FriendListResponseDto {

    private final Long friendId;
    private final Long friendMemberId;
    private final String friendName;
    private final String friendNickname;
    private final String friendProfileImageUrl;
    private final String friendEmail;

    public static FriendListResponseDto convertToFriendListResponseDto(Friend friend, Member member) {
        return FriendListResponseDto.builder()
                .friendId(friend.getId())
                .friendMemberId(member.getId())
                .friendName(member.getName())
                .friendNickname(member.getNickname())
                .friendProfileImageUrl(member.getProfileImageUrl())
                .friendEmail(member.getEmail())
                .build();
    }
}
