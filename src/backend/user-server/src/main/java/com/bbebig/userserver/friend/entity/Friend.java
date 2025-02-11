package com.bbebig.userserver.friend.entity;

import com.bbebig.commonmodule.global.common.BaseTimeEntity;
import com.bbebig.userserver.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Friend extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member fromMember;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member toMember;

    @Enumerated(EnumType.STRING)
    private FriendStatus friendStatus;

    @Column(columnDefinition = "JSON")
    private String serverList;

    public void updateFriendStatus(FriendStatus friendStatus) {
        this.friendStatus = friendStatus;
    }
}
