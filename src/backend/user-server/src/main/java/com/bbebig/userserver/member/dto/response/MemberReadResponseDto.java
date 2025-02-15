package com.bbebig.userserver.member.dto.response;

import com.bbebig.commonmodule.kafka.dto.model.PresenceType;
import com.bbebig.userserver.member.entity.CustomPresenceStatus;
import com.bbebig.userserver.member.entity.Member;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class MemberReadResponseDto {

    private final Long id;
    private final String name;
    private final String nickname;
    private final String email;
    private final LocalDate birthdate;
    private final String avatarUrl;
    private final String bannerUrl;
    private final String introduce;
    private final PresenceType customPresenceStatus;
    private final LocalDateTime lastAccessAt;

    public static MemberReadResponseDto convertToMemberReadResponseDto(Member member) {
        return MemberReadResponseDto.builder()
                .id(member.getId())
                .name(member.getName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .birthdate(member.getBirthdate())
                .avatarUrl(member.getAvatarUrl())
                .bannerUrl(member.getBannerUrl())
                .introduce(member.getIntroduce())
                .customPresenceStatus(member.getCustomPresenceStatus())
                .lastAccessAt(member.getLastAccessAt())
                .build();
    }
}
