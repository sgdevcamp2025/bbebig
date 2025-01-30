package com.bbebig.serviceserver.server.entity;

import com.bbebig.commonmodule.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 서버에 속한 멤버의 테이블을 관리하는 엔티티 클래스입니다.
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ServerMember extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "server_id")
    private Long serverId;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "member_nickname")
    private String memberNickname;

    @Lob
    @Column(name = "member_profile_image_url")
    private String memberProfileImageUrl;

    @Column(name = "role")
    private Role role;
}
