package com.bbebig.user_server.application;

import com.bbebig.user_server.domain.*;
import com.bbebig.user_server.presentation.dto.FriendCreateRequest;
import com.bbebig.user_server.presentation.dto.FriendCreateResponse;
import com.bbebig.user_server.presentation.dto.FriendResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class FriendService {
    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;

    public void createFriend(Long memberId, FriendCreateRequest request) {
        //수락상태가 아닌거만 조회
        Optional<Friend> existFriend = friendRepository.findByStatusNot(FriendStatus.ACCEPTED);
        if (existFriend.isPresent()) {
            //있으면 pending으로 변경
            existFriend.get().changeFriendStatus(FriendStatus.PENDING);
        } else {
            //없으면 생성
            Member fromMember = memberRepository.findById(memberId).get();
            Member toMember = memberRepository.findById(request.toMemberId()).get();
            friendRepository.save(Friend.of(fromMember, toMember));
        }
    }

    public Page<FriendCreateResponse> getFriendCreate(Long memberId, Pageable pageRequest) {
        Member member = memberRepository.findById(memberId).get();
        return friendRepository.findByToMemberAndStatus(member, FriendStatus.PENDING, pageRequest).map(FriendCreateResponse::of);
    }

    public Page<FriendResponse> getFriendList(Long memberId, Pageable pageRequest) {
        Member member = memberRepository.findById(memberId).get();
        return friendRepository.findByStatusAndToMemberOrFromMember(FriendStatus.ACCEPTED, member, member, pageRequest).map(FriendResponse::of);
    }

    public void changeFriendCreateStatus(Long friendId, FriendStatus requestStatus) {
        friendRepository.findById(friendId).get().changeFriendStatus(requestStatus);
    }
}
