package com.bbebig.userserver.member.service;

import com.bbebig.commonmodule.clientDto.userServer.CommonUserServerResponseDto;
import com.bbebig.commonmodule.clientDto.userServer.CommonUserServerResponseDto.MemberGlobalStatusResponseDto;
import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import com.bbebig.userserver.friend.repository.FriendRepository;
import com.bbebig.userserver.member.dto.request.MemberPresenceUpdateRequestDto;
import com.bbebig.userserver.member.dto.request.MemberUpdateRequestDto;
import com.bbebig.userserver.member.dto.response.MemberDeleteResponseDto;
import com.bbebig.userserver.member.dto.response.MemberPresenceUpdateResponseDto;
import com.bbebig.userserver.member.dto.response.MemberReadResponseDto;
import com.bbebig.userserver.member.dto.response.MemberUpdateResponseDto;
import com.bbebig.userserver.member.entity.Member;
import com.bbebig.userserver.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;

    /**
     * 멤버 정보 업데이트
     */
    @Transactional
    public MemberUpdateResponseDto updateMember(Long memberId, MemberUpdateRequestDto memberUpdateRequestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        member.updateInfo(memberUpdateRequestDto);

        return MemberUpdateResponseDto.convertToMemberUpdateResponseDto(member);
    }

    /**
     * 멤버 접속 정보 업데이트
     */
    @Transactional
    public MemberPresenceUpdateResponseDto updateMemberPresence(Long memberId, MemberPresenceUpdateRequestDto memberPresenceUpdateRequestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        member.updateCustomPresenceStatus(memberPresenceUpdateRequestDto.getCustomPresenceStatus());

        return MemberPresenceUpdateResponseDto.convertToMemberPresenceUpdateResponseDto(member);
    }

    /**
     * 멤버 삭제 (탈퇴)
     */
    @Transactional
    public MemberDeleteResponseDto deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));


        friendRepository.deleteAllByFromMemberOrToMember(member, member);

        memberRepository.delete(member);

        return MemberDeleteResponseDto.convertToMemberDeleteResponseDto(member);
    }

    /**
     * 멤버 정보 조회
     */
    @Transactional(readOnly = true)
    public MemberReadResponseDto readMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        return MemberReadResponseDto.convertToMemberReadResponseDto(member);
    }

    /**
     * 멤버 전역 상태 조회
     */
    public MemberGlobalStatusResponseDto getMemberGlobalStatus(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        return MemberGlobalStatusResponseDto.builder()
                .memberId(memberId)
                .globalStatus(member.getCustomPresenceStatus())
                .build();
    }
}
