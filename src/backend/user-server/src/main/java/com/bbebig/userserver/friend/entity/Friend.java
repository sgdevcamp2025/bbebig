package com.bbebig.userserver.friend.entity;

import com.bbebig.userserver.member.entity.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "friend")
public class Friend {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user_id")
    private Member fromMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user_id")
    private Member toMember;

    @Enumerated(EnumType.STRING)
    private FriendStatus status;

    @Column(columnDefinition = "JSON", name = "server_list")
    private String serverList;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Builder
    public Friend(Member fromMember, Member toMember, FriendStatus status, String serverList) {
        this.fromMember = fromMember;
        this.toMember = toMember;
        this.status = status;
        this.serverList = serverList;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void changeFriendStatus(FriendStatus status){
        this.status=status;
    }


    public static Friend of(Member fromMember, Member toMember){
        return Friend.builder().fromMember(fromMember).toMember(toMember).status(FriendStatus.PENDING).build();
    }
}
