package com.bbebig.userserver.member.controller;

import com.bbebig.userserver.member.service.MemberService;
import com.bbebig.userserver.member.entity.Member;
import com.bbebig.userserver.member.dto.request.MemberModifyRequest;
import com.bbebig.userserver.member.dto.response.MemberSearchResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("/{memberId}")
    public ResponseEntity searchMember(@PathVariable Long memberId) {
        Member member = memberService.searchMember(memberId);
        return ResponseEntity.ok(MemberSearchResponse.of(member));
    }

    @PutMapping("/{memberId}")
    public ResponseEntity modifyMemberInfo(@PathVariable Long memberId, @RequestBody MemberModifyRequest request) {
        memberService.modifyMemberInfo(memberId, request);
        return ResponseEntity.noContent().build();
    }
}
