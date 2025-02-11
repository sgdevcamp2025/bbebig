package com.bbebig.userserver.friend.dto.response;

import com.bbebig.userserver.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FriendListResponseDto {

    private final Long friendMemberId;
    private final String friendNickname;
    private final String friendProfileImageUrl;
    private final String friendEmail;

    public static FriendListResponseDto convertToFriendListResponseDto(Member member) {
        return FriendListResponseDto.builder()
                .friendMemberId(member.getId())
                .friendNickname(member.getNickname())
                .friendProfileImageUrl(member.getProfileImageUrl())
                .friendEmail(member.getEmail())
                .build();
    }
}
