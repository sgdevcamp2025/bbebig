package com.bbebig.serviceserver.channel.entity;

import com.bbebig.commonmodule.global.common.BaseTimeEntity;
import com.bbebig.serviceserver.category.entity.Category;
import com.bbebig.serviceserver.channel.dto.request.ChannelUpdateRequestDto;
import com.bbebig.serviceserver.server.entity.Server;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 채널의 테이블을 관리하는 엔티티 클래스입니다.
 */
@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Channel extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "server_id")
    private Server server;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "name")
    private String name;

    @Column(name = "position")
    private int position;

    @Enumerated(EnumType.STRING)
    @Column(name = "channel_type")
    private ChannelType channelType;

    @Column(name = "private_status")
    private boolean privateStatus;

    public void update(ChannelUpdateRequestDto channelUpdateRequestDto) {
        this.name = channelUpdateRequestDto.getChannelName();
        this.privateStatus = channelUpdateRequestDto.isPrivateStatus();
    }

    public void updateCategoryNull() {
        this.category = null;
    }

    public void updatePosition(int position) {
        this.position = position;
    }
}
