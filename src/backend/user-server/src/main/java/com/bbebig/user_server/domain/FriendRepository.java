package com.bbebig.user_server.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend,Long> {
    Optional<Friend> findByStatusNot(FriendStatus status);

    Page<Friend> findByToMemberAndStatus(Member member, FriendStatus status, Pageable pageable);
}
