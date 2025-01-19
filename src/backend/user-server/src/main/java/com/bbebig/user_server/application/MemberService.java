package com.bbebig.user_server.application;

import com.bbebig.user_server.domain.Member;
import com.bbebig.user_server.domain.MemberRepository;
import com.bbebig.user_server.presentation.dto.MemberModifyRequest;
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
