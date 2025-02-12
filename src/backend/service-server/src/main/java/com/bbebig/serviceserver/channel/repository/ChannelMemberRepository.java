package com.bbebig.serviceserver.channel.repository;

import com.bbebig.serviceserver.channel.entity.Channel;
import com.bbebig.serviceserver.channel.entity.ChannelMember;
import com.bbebig.serviceserver.server.entity.ServerMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChannelMemberRepository extends JpaRepository<ChannelMember, Long> {
    void deleteAllByChannel(Channel channel);

    boolean existsByChannelIdAndServerMember(Long channelId, ServerMember serverMember);

    void deleteAllByChannelId(Long channelId);

    void deleteAllByServerMember(ServerMember serverMember);

    Optional<ChannelMember> findByServerMemberIdAndChannelId(Long serverMemberId, Long channelId);

    List<ChannelMember> findAllByChannelId(Long channelId);
}
