package com.bbebig.user_server.presentation;

import com.bbebig.user_server.application.FriendService;
import com.bbebig.user_server.presentation.dto.FriendCreateRequest;
import com.bbebig.user_server.presentation.dto.MemberModifyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friend")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @PostMapping("")
    public ResponseEntity createFriend(@RequestParam Long memberId, @RequestBody FriendCreateRequest request) {
        friendService.createFriend(memberId, request);
        return ResponseEntity.noContent().build();
    }
}
