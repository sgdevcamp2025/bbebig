package com.bbebig.serviceserver.server.repository;

import com.bbebig.serviceserver.server.entity.Server;
import com.bbebig.serviceserver.server.entity.ServerMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ServerMemberRepository extends JpaRepository<ServerMember, Long> {
    boolean existsByServerIdAndMemberId(Long serverId, Long memberId);

    List<ServerMember> findAllByMemberId(Long memberId);

    // 특정 서버 내 멤버 ID 리스트를 기준으로 ServerMember 찾기 (비공개 채널용)
    List<ServerMember> findByServerAndMemberIdIn(Server server, List<Long> memberIds);

    // 특정 서버의 모든 멤버 조회 (공개 채널용)
    List<ServerMember> findByServer(Server server);

    Optional<ServerMember> findByMemberIdAndServer(Long memberId, Server server);
}
