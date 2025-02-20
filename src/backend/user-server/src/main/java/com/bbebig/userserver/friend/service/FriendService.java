package com.bbebig.userserver.friend.service;

import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import com.bbebig.commonmodule.kafka.dto.model.PresenceType;
import com.bbebig.commonmodule.kafka.dto.notification.FriendActionEventDto;
import com.bbebig.commonmodule.kafka.dto.notification.NotificationEventType;
import com.bbebig.commonmodule.kafka.dto.notification.status.FriendActionStatus;
import com.bbebig.commonmodule.redis.domain.MemberPresenceStatus;
import com.bbebig.userserver.friend.dto.response.*;
import com.bbebig.userserver.friend.dto.response.FriendResponseDto.FriendInfoResponseDto;
import com.bbebig.userserver.friend.dto.response.FriendResponseDto.FriendListResponseDto;
import com.bbebig.userserver.friend.dto.response.FriendResponseDto.PendingFriendInfoResponseDto;
import com.bbebig.userserver.friend.dto.response.FriendResponseDto.PendingFriendListResponseDto;
import com.bbebig.userserver.friend.entity.Friend;
import com.bbebig.userserver.friend.entity.FriendStatus;
import com.bbebig.userserver.friend.repository.FriendRepository;
import com.bbebig.userserver.member.entity.Member;
import com.bbebig.userserver.member.repository.MemberRedisRepositoryImpl;
import com.bbebig.userserver.member.repository.MemberRepository;
import com.bbebig.userserver.friend.dto.request.FriendCreateRequestDto;
import com.bbebig.userserver.member.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FriendService {

    private final FriendRepository friendRepository;
    private final MemberRepository memberRepository;

    private final MemberRedisRepositoryImpl memberRedisRepository;
    private final KafkaProducerService kafkaProducerService;

    /**
     * 친구 요청 (생성)
     */
    @Transactional
    public FriendCreateResponseDto createFriend(Long memberId, FriendCreateRequestDto friendCreateRequestDto) {
        Member fromMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Member toMember = memberRepository.findById(friendCreateRequestDto.getToMemberId())
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 본인에게 요청을 보낸 경우
        if (fromMember.equals(toMember)) {
            throw new ErrorHandler(ErrorStatus.FRIEND_REQUEST_SELF);
        }

        Optional<Friend> optionalFriend = friendRepository.findFriendMember1AndMember2(fromMember, toMember);

        // 친구 관계가 이미 존재할 때
        if (optionalFriend.isPresent()) {
            Friend friend = optionalFriend.get();

            if (friend.getFriendStatus() == FriendStatus.DECLINED) {
                friend.updateFriendStatus(FriendStatus.PENDING);
                return FriendCreateResponseDto.convertToFriendCreateResponseDto(friend);
            }
            throw new ErrorHandler(ErrorStatus.FRIEND_RELATION_EXIST);
        }

        Friend friend = Friend.builder()
                .fromMember(fromMember)
                .toMember(toMember)
                .friendStatus(FriendStatus.PENDING)
//                .serverList() TODO: 같이 있는 서버 조회 (FeignClient)
                .build();

        friendRepository.save(friend);

        FriendActionEventDto pendingDto = FriendActionEventDto.builder()
                .memberId(fromMember.getId())
                .type(NotificationEventType.FRIEND_ACTION)
                .friendId(friend.getId())
                .friendMemberId(toMember.getId())
                .friendNickName(toMember.getNickname())
                .friendAvatarUrl(toMember.getAvatarUrl())
                .friendBannerUrl(toMember.getBannerUrl())
                .status(FriendActionStatus.PENDING)
                .build();

        FriendActionEventDto receiveDto = FriendActionEventDto.builder()
                .memberId(toMember.getId())
                .type(NotificationEventType.FRIEND_ACTION)
                .friendId(friend.getId())
                .friendMemberId(fromMember.getId())
                .friendNickName(fromMember.getNickname())
                .friendAvatarUrl(fromMember.getAvatarUrl())
                .friendBannerUrl(fromMember.getBannerUrl())
                .status(FriendActionStatus.RECEIVE)
                .build();
        kafkaProducerService.sendNotificationEvent(pendingDto);
        kafkaProducerService.sendNotificationEvent(receiveDto);

        return FriendCreateResponseDto.convertToFriendCreateResponseDto(friend);
    }

    /**
     * 요청 대기 중인 친구 목록 조회
     */
    @Transactional(readOnly = true)
    public PendingFriendListResponseDto getPendingFriends(Long memberId) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<Friend> friendList = friendRepository.findFriendsByMemberIdAndStatus(memberId, FriendStatus.PENDING);
        List<PendingFriendInfoResponseDto> sendPendingFriends = new ArrayList<>();
        List<PendingFriendInfoResponseDto> receivePendingFriends = new ArrayList<>();

        for (Friend friend : friendList) {
            if (friend.getFromMember().getId().equals(memberId)) {
                PendingFriendInfoResponseDto dto = FriendResponseDto.convertToPendingFriendListResponseDto(friend, friend.getToMember());
                sendPendingFriends.add(dto);
            } else {
                PendingFriendInfoResponseDto dto = FriendResponseDto.convertToPendingFriendListResponseDto(friend, friend.getFromMember());
                receivePendingFriends.add(dto);
            }
        }
        return PendingFriendListResponseDto.builder()
                .memberId(memberId)
                .pendingFriendsCount(sendPendingFriends.size())
                .receivePendingFriendsCount(receivePendingFriends.size())
                .sendPendingFriends(sendPendingFriends)
                .receivePendingFriends(receivePendingFriends)
                .build();

    }

    /**
     * 요청 수락한 친구 목록 조회
     */
    @Transactional(readOnly = true)
    public FriendListResponseDto getAcceptedFriends(Long memberId) {
        memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<Friend> friendList = friendRepository.findFriendsByMemberIdAndStatus(memberId, FriendStatus.ACCEPTED);
        List<FriendInfoResponseDto> friends = friendList.stream()
                .map(friend -> {
                    Member friendMember = friend.getFromMember().getId().equals(memberId)
                            ? friend.getToMember() : friend.getFromMember();
                    MemberPresenceStatus memberPresenceStatus = memberRedisRepository.getMemberPresenceStatus(friendMember.getId());
                    if (memberPresenceStatus == null) {
                        memberPresenceStatus = MemberPresenceStatus.builder()
                                .memberId(friendMember.getId())
                                .globalStatus(friendMember.getCustomPresenceStatus())
                                .actualStatus(PresenceType.OFFLINE)
                                .build();
                    }
                    return FriendResponseDto.convertToFriendListResponseDto(friend, friendMember, memberPresenceStatus);
                })
                .toList();

        return FriendListResponseDto.builder()
                .memberId(memberId)
                .friendsCount(friends.size())
                .friends(friends)
                .build();
    }

    /**
     * 친구 요청 수락/거절
     */
    @Transactional
    public FriendUpdateResponseDto updateFriendStatus(Long memberId, Long friendId, FriendStatus friendStatus) {
        Friend friend = friendRepository.findById(friendId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.FRIEND_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Member friendMember = memberRepository.findById(friend.getFromMember().getId())
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 친구 요청을 받은 사람이 본인인지 확인
        validateToMember(memberId, friend);

        // 친구 요청 상태가 아닐 경우
        if (friend.getFriendStatus() != FriendStatus.PENDING) {
            throw new ErrorHandler(ErrorStatus.FRIEND_REQUEST_NOT_FOUND);
        }

        friend.updateFriendStatus(friendStatus);

        if (friendStatus == FriendStatus.ACCEPTED) {
            if (memberRedisRepository.existsMemberFriendList(friendMember.getId())) {
                memberRedisRepository.addMemberFriendToSet(friendMember.getId(), friendMember.getId());
            } else {
                makeMemberFriendListCache(friendMember.getId());
            }

            if (memberRedisRepository.existsMemberFriendList(member.getId())) {
                memberRedisRepository.addMemberFriendToSet(member.getId(), member.getId());
            } else {
                makeMemberFriendListCache(member.getId());
            }

            FriendActionEventDto friendActionEventDto = FriendActionEventDto.builder()
                    .memberId(friendMember.getId())
                    .type(NotificationEventType.FRIEND_ACTION)
                    .friendId(friend.getId())
                    .friendMemberId(member.getId())
                    .friendAvatarUrl(member.getAvatarUrl())
                    .friendBannerUrl(member.getBannerUrl())
                    .friendNickName(member.getNickname())
                    .status(FriendActionStatus.ACCEPT)
                    .build();

            FriendActionEventDto myActionEventDto = FriendActionEventDto.builder()
                    .memberId(member.getId())
                    .friendId(friend.getId())
                    .type(NotificationEventType.FRIEND_ACTION)
                    .friendMemberId(friendMember.getId())
                    .friendAvatarUrl(friendMember.getAvatarUrl())
                    .friendBannerUrl(friendMember.getBannerUrl())
                    .friendNickName(friendMember.getNickname())
                    .status(FriendActionStatus.ACCEPT)
                    .build();

            kafkaProducerService.sendNotificationEvent(friendActionEventDto);
            kafkaProducerService.sendNotificationEvent(myActionEventDto);

        }

        // TODO: 친구 요청 거절 로직


        return FriendUpdateResponseDto.convertToFriendUpdateResponseDto(friend);
    }

    /**
     * 친구 삭제
     */
    @Transactional
    public FriendDeleteResponseDto deleteFriend(Long memberId, Long friendId) {
        Friend friend = friendRepository.findById(friendId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.FRIEND_NOT_FOUND));

        // 본인의 친구 관계인지 확인
        checkOwnRelation(memberId, friend);

        // 친구가 아닐 경우
        if (friend.getFriendStatus() != FriendStatus.ACCEPTED) {
            throw new ErrorHandler(ErrorStatus.FRIEND_RELATION_NOT_EXIST);
        }

        friendRepository.delete(friend);
        memberRedisRepository.removeMemberFriendFromSet(friend.getFromMember().getId(), friend.getToMember().getId());
        memberRedisRepository.removeMemberFriendFromSet(friend.getToMember().getId(), friend.getFromMember().getId());

        FriendActionEventDto friendActionEventDto = FriendActionEventDto.builder()
                .memberId(friend.getFromMember().getId())
                .type(NotificationEventType.FRIEND_ACTION)
                .friendId(friend.getId())
                .friendMemberId(friend.getToMember().getId())
                .status(FriendActionStatus.DELETE)
                .build();

        FriendActionEventDto myActionEventDto = FriendActionEventDto.builder()
                .memberId(friend.getToMember().getId())
                .type(NotificationEventType.FRIEND_ACTION)
                .friendId(friend.getId())
                .friendMemberId(friend.getFromMember().getId())
                .status(FriendActionStatus.DELETE)
                .build();

        kafkaProducerService.sendNotificationEvent(friendActionEventDto);
        kafkaProducerService.sendNotificationEvent(myActionEventDto);


        return FriendDeleteResponseDto.convertToFriendDeleteResponseDto(friend);
    }

    /**
     * 친구 요청 취소
     */
    @Transactional
    public FriendDeleteResponseDto cancelFriendPending(Long memberId, Long friendId) {
        Friend friend = friendRepository.findById(friendId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.FRIEND_NOT_FOUND));

        // 친구 요청을 보낸 사람이 본인인지 확인
        validateFromMember(memberId, friend);

        // 친구 요청을 보낸 상태가 아닐 경우
        if (friend.getFriendStatus() != FriendStatus.PENDING) {
            throw new ErrorHandler(ErrorStatus.FRIEND_REQUEST_NOT_PENDING);
        }

        friendRepository.delete(friend);

        FriendActionEventDto friendRequestEventDto = FriendActionEventDto.builder()
                .memberId(friend.getToMember().getId())
                .type(NotificationEventType.FRIEND_ACTION)
                .friendId(friend.getId())
                .friendMemberId(memberId)
                .status(FriendActionStatus.CANCEL)
                .build();

        FriendActionEventDto myRequestEventDto = FriendActionEventDto.builder()
                .memberId(memberId)
                .type(NotificationEventType.FRIEND_ACTION)
                .friendId(friend.getId())
                .friendMemberId(friend.getToMember().getId())
                .status(FriendActionStatus.CANCEL)
                .build();

        kafkaProducerService.sendNotificationEvent(friendRequestEventDto);
        kafkaProducerService.sendNotificationEvent(myRequestEventDto);

        return FriendDeleteResponseDto.convertToFriendDeleteResponseDto(friend);
    }

    public List<Long> getMemberFriendIdList(Long memberId) {
        if (memberRedisRepository.existsMemberFriendList(memberId)) {
            return memberRedisRepository.getMemberFriendList(memberId).stream().toList();
        } else {
            return makeMemberFriendListCache(memberId);
        }
    }

    public List<Long> makeMemberFriendListCache(Long memberId) {
        List<Friend> friendList = friendRepository.findFriendsByMemberIdAndStatus(memberId, FriendStatus.ACCEPTED);
        List<Long> friendIdList = friendList.stream()
                .map(friend -> {
                    return friend.getFromMember().getId().equals(memberId)
                            ? friend.getToMember().getId() : friend.getFromMember().getId();
                })
                .toList();
        if (friendIdList.isEmpty()) {
            return List.of();
        }
        memberRedisRepository.saveMemberFriendSet(memberId, friendIdList);
        return friendIdList;
    }

    /**
     * 본인의 친구 관계인지 확인
     */
    private void checkOwnRelation(Long memberId, Friend friend) {
        if (!friend.getFromMember().getId().equals(memberId) && !friend.getToMember().getId().equals(memberId)) {
            throw new ErrorHandler(ErrorStatus.UNAUTHORIZED_FRIEND_ACTION);
        }
    }

    /**
     * 친구 요청을 받은 사람이 본인인지 확인
     */
    private void validateToMember(Long memberId, Friend friend) {
        if (!friend.getToMember().getId().equals(memberId)) {
            throw new ErrorHandler(ErrorStatus.UNAUTHORIZED_FRIEND_ACTION);
        }
    }

    /**
     * 친구 요청을 보낸 사람이 본인인지 확인
     */
    private void validateFromMember(Long memberId, Friend friend) {
        if (!friend.getFromMember().getId().equals(memberId)) {
            throw new ErrorHandler(ErrorStatus.UNAUTHORIZED_FRIEND_ACTION);
        }
    }
}
