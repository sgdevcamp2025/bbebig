package com.bbebig.userserver.member.repository;

import com.bbebig.userserver.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

	Optional<Member> findByNickname(String nickname);
}
