package com.bbebig.userserver.friend.repository;

import com.bbebig.userserver.friend.entity.Friend;
import com.bbebig.userserver.friend.entity.FriendStatus;
import com.bbebig.userserver.member.entity.Member;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface FriendRepository extends JpaRepository<Friend, Long> {
    @Query("SELECT CASE WHEN f.fromMember.id = :memberId THEN f.toMember ELSE f.fromMember END " +
            "FROM Friend f WHERE (f.fromMember.id = :memberId OR f.toMember.id = :memberId) " +
            "AND f.friendStatus = :status")
    List<Member> findFriendMembersByMemberIdAndStatus(@Param("memberId") Long memberId,
                                                      @Param("status") FriendStatus status);

    @Query("SELECT f FROM Friend f WHERE (f.fromMember = :member1 AND f.toMember = :member2) " +
            "OR (f.fromMember = :member2 AND f.toMember = :member1)")
    Optional<Friend> findFriendMember1AndMember2(@Param("member1") Member member1,
                                        @Param("member2") Member member2);

    void deleteAllByFromMemberOrToMember(Member fromMember, Member toMember);
}
