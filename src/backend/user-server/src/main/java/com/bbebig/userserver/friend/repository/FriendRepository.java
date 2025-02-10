package com.bbebig.userserver.friend.repository;

import com.bbebig.userserver.friend.entity.Friend;
import com.bbebig.userserver.friend.entity.FriendStatus;
import com.bbebig.userserver.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend,Long> {
    Optional<Friend> findByStatusNot(FriendStatus status);

    Page<Friend> findByToMemberAndStatus(Member member, FriendStatus status, Pageable pageable);
    Page<Friend> findByStatusAndToMemberOrFromMember( FriendStatus status,Member toMember, Member fromMember, Pageable pageable);
}
