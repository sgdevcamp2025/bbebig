package com.bbebig.serviceserver.channel.entity;

import com.bbebig.commonmodule.global.common.BaseTimeEntity;
import com.bbebig.serviceserver.server.entity.ServerMember;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 채널에 속한 멤버의 테이블을 관리하는 엔티티 클래스입니다.
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChannelMember extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "channel_id")
    private Channel channel;

    @ManyToOne
    @JoinColumn(name = "server_member_id")
    private ServerMember serverMember;

    private Long lastReadMessageId;

    private Long lastReadSequence;

    private LocalDateTime lastAccessAt;

    public void updateLastInfo(Long lastReadMessageId, Long lastReadSequence,LocalDateTime lastAccessAt) {
        this.lastReadMessageId = lastReadMessageId;
        this.lastReadSequence = lastReadSequence;
        this.lastAccessAt = lastAccessAt;
    }
}
