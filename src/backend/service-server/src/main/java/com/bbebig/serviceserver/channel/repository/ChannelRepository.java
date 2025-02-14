package com.bbebig.serviceserver.channel.repository;

import com.bbebig.serviceserver.category.entity.Category;
import com.bbebig.serviceserver.channel.entity.Channel;
import com.bbebig.serviceserver.server.entity.Server;
import com.bbebig.serviceserver.server.entity.ServerMember;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
    // 특정 서버와 카테고리에 속한 채널 중 position 최대값 찾기
    @Query("SELECT COALESCE(MAX(c.position), 0) FROM Channel c WHERE c.server = :server AND c.category = :category")
    Optional<Integer> findMaxPositionByServerAndCategory(@Param("server") Server server, @Param("category") Category category);

    // 특정 서버에서 카테고리가 없는 채널 중 position 최대값 찾기
    @Query("SELECT COALESCE(MAX(c.position), 0) FROM Channel c WHERE c.server = :server AND c.category IS NULL")
    Optional<Integer> findMaxPositionByServerAndCategoryIsNull(@Param("server") Server server);

    List<Channel> findAllByCategory(Category category);

    List<Channel> findAllByServerId(Long serverId);

    List<Channel> findAllByServerIdAndPrivateStatusFalse(Long serverId);
}