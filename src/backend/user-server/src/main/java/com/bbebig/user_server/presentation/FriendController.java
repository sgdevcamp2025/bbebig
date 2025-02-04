package com.bbebig.user_server.presentation;

import com.bbebig.user_server.application.FriendService;
import com.bbebig.user_server.domain.FriendStatus;
import com.bbebig.user_server.presentation.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/friend")
@RequiredArgsConstructor
public class FriendController {
    private final FriendService friendService;

    @PostMapping("")
    public ResponseEntity createFriend(@RequestHeader("memberId") Long memberId, @RequestBody FriendCreateRequest request) {
        friendService.createFriend(memberId, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("")
    public ResponseEntity getFriendList(@RequestHeader("memberId") Long memberId, @ModelAttribute PageRequestDto request) {
        Page<FriendResponse> response= friendService.getFriendList(memberId, request.toPageRequest());
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/request")
    public ResponseEntity getFriendCreate(@RequestHeader("memberId") Long memberId, @ModelAttribute PageRequestDto request) {
        Page<FriendCreateResponse> response= friendService.getFriendCreate(memberId, request.toPageRequest());
        return ResponseEntity.ok().body(response);
    }
    @DeleteMapping("/{friendId}")
    public ResponseEntity deleteFriend(@RequestHeader("memberId") Long memberId,@PathVariable("friendId")Long friendId) {
        friendService.deleteFriend(friendId);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/request/{friendId}")
    public ResponseEntity changeFriendCreateStatus(@RequestHeader("memberId") Long memberId,@PathVariable("friendId")Long friendId, @RequestParam FriendStatus requestStatus) {
        friendService.changeFriendCreateStatus(friendId,requestStatus);
        return ResponseEntity.noContent().build();
    }
}
