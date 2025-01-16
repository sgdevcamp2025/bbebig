package com.bbebig.user_server.domain;

import com.bbebig.user_server.presentation.dto.UserModifyRequest;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String email;

    @Column
    private String password;

    @Column(name = "profile_img_url")
    private String profileImgUrl;

    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "custom_presence_status")
    private CustomPresenceStatus customPresenceStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "last_access_at")
    private LocalDateTime lastAccessAt;

    public void modify(UserModifyRequest request) {
        if(request.profileImgUrl()!=null) this.profileImgUrl=request.profileImgUrl();
        if(request.nickname()!=null)this.nickname=request.nickname();
    }
}