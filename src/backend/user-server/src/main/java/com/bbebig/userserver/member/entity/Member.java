package com.bbebig.userserver.member.entity;

import com.bbebig.commonmodule.global.common.BaseTimeEntity;
import com.bbebig.commonmodule.kafka.dto.model.PresenceType;
import com.bbebig.userserver.member.dto.request.MemberUpdateRequestDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String nickname;

    private String email;

    private String password;

    private String avatarUrl;

    private String bannerUrl;

    private String introduce;

    private LocalDate birthdate;

    @Enumerated(EnumType.STRING)
    private PresenceType customPresenceStatus;

    private LocalDateTime lastAccessAt;

    public void updateInfo(MemberUpdateRequestDto memberUpdateRequestDto) {
        this.name = memberUpdateRequestDto.getName();
        this.nickname = memberUpdateRequestDto.getNickname();
        this.birthdate = memberUpdateRequestDto.getBirthdate();
        this.introduce = memberUpdateRequestDto.getIntroduce();
        this.avatarUrl = memberUpdateRequestDto.getAvatarUrl();
        this.bannerUrl = memberUpdateRequestDto.getBannerUrl();
    }

    public void updateCustomPresenceStatus(PresenceType presenceType) {
        this.customPresenceStatus = presenceType;
    }
}