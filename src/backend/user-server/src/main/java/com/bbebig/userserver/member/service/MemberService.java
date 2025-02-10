package com.bbebig.userserver.member.service;

import com.bbebig.userserver.member.entity.Member;
import com.bbebig.userserver.member.repository.MemberRepository;
import com.bbebig.userserver.member.dto.request.MemberModifyRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    public Member searchMember(Long memberId) {
        return memberRepository.findById(memberId).get();
    }

    public void modifyMemberInfo(Long memberId, MemberModifyRequest request) {
        memberRepository.findById(memberId).get().modify(request);
    }
}
