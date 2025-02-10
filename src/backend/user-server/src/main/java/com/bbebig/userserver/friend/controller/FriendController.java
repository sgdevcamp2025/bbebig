package com.bbebig.userserver.friend.controller;

import com.bbebig.userserver.friend.dto.request.FriendCreateRequest;
import com.bbebig.userserver.friend.dto.request.PageRequestDto;
import com.bbebig.userserver.friend.dto.response.FriendCreateResponse;
import com.bbebig.userserver.friend.dto.response.FriendResponse;
import com.bbebig.userserver.friend.service.FriendService;
import com.bbebig.userserver.friend.entity.FriendStatus;
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
