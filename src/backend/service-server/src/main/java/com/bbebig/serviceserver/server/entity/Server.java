package com.bbebig.serviceserver.server.entity;

import com.bbebig.commonmodule.global.common.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 서버의 테이블을 관리하는 엔티티 클래스입니다.
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Server extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "server_image_url", length = 1024)
    private String serverImageUrl;

    // 서버의 이름 업데이트
    public void updateName(String name) {
        this.name = name;
    }

    // 서버의 이미지 업데이트
    public void updateServerImageUrl(String serverImageUrl) {
        this.serverImageUrl = serverImageUrl;
    }
}
