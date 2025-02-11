package com.bbebig.userserver.member.entity;

import com.bbebig.commonmodule.global.common.BaseTimeEntity;
import com.bbebig.userserver.member.dto.request.MemberModifyRequest;
import jakarta.persistence.*;
import lombok.*;

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

    private String email;

    private String password;

    private String birthdate;

    private String profileImageUrl;

    private String nickname;

    @Enumerated(EnumType.STRING)
    private CustomPresenceStatus customPresenceStatus;

    private LocalDateTime lastAccessAt;

    public void modify(MemberModifyRequest request) {
        if (request.profileImgUrl() != null) {
            this.profileImageUrl = request.profileImgUrl();
        }

        if (request.nickname() != null) {
            this.nickname = request.nickname();
        }
    }
}