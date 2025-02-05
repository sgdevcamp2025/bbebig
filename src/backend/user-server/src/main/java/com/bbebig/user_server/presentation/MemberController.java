package com.bbebig.user_server.presentation;

import com.bbebig.user_server.application.MemberService;
import com.bbebig.user_server.domain.Member;
import com.bbebig.user_server.presentation.dto.MemberModifyRequest;
import com.bbebig.user_server.presentation.dto.MemberSearchResponse;
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
