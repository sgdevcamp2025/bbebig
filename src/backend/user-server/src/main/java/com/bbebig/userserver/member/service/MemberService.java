package com.bbebig.userserver.member.service;

import com.bbebig.commonmodule.clientDto.userServer.CommonUserServerResponseDto.MemberGlobalStatusResponseDto;
import com.bbebig.commonmodule.clientDto.userServer.CommonUserServerResponseDto.MemberInfoResponseDto;
import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import com.bbebig.commonmodule.kafka.dto.MemberEventDto;
import com.bbebig.commonmodule.kafka.dto.notification.FriendActionEventDto;
import com.bbebig.commonmodule.kafka.dto.notification.NotificationEventType;
import com.bbebig.userserver.friend.entity.Friend;
import com.bbebig.userserver.friend.entity.FriendStatus;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final FriendRepository friendRepository;
    private final KafkaProducerService kafkaProducerService;

    /**
     * 멤버 정보 업데이트
     */
    @Transactional
    public MemberUpdateResponseDto updateMember(Long memberId, MemberUpdateRequestDto memberUpdateRequestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        member.updateInfo(memberUpdateRequestDto);

        List<Friend> friendList = friendRepository.findFriendsByMemberIdAndStatus(memberId, FriendStatus.ACCEPTED);
        for (Friend friend : friendList) {
            Member friendMember;
            if (friend.getFromMember().getId() == memberId) {
                friendMember = friend.getToMember();
            } else {
                friendMember = friend.getFromMember();
            }
            // 친구 관계 업데이트 이벤트 발행
            FriendActionEventDto friendActionEventDto = FriendActionEventDto.builder()
                    .memberId(memberId)
                    .type(NotificationEventType.FRIEND_ACTION)
                    .friendMemberId(friendMember.getId())
                    .friendNickName(friendMember.getNickname())
                    .friendAvatarUrl(friendMember.getAvatarUrl())
                    .friendBannerUrl(friendMember.getBannerUrl())
                    .status("UPDATE")
                    .build();
            kafkaProducerService.sendNotificationEvent(friendActionEventDto);
        }

        // Kafka로 이벤트 발행
        MemberEventDto memberEventDto = MemberEventDto.builder()
                .memberId(memberId)
                .type("DELETE")
                .nickname(member.getNickname())
                .avatarUrl(member.getAvatarUrl())
                .bannerUrl(member.getBannerUrl())
                .build();
        kafkaProducerService.sendMemberEvent(memberEventDto);

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

        MemberEventDto memberEventDto = MemberEventDto.builder()
                .memberId(memberId)
                .type("PRESENCE_UPDATE")
                .globalStatus(member.getCustomPresenceStatus())
                .build();
        kafkaProducerService.sendMemberEvent(memberEventDto);

        return MemberPresenceUpdateResponseDto.convertToMemberPresenceUpdateResponseDto(member);
    }

    /**
     * 멤버 삭제 (탈퇴)
     */
    @Transactional
    public MemberDeleteResponseDto deleteMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<Friend> friendList = friendRepository.findFriendsByMemberIdAndStatus(memberId, FriendStatus.ACCEPTED);
        for (Friend friend : friendList) {
            // 친구 관계 삭제 이벤트 발행
            FriendActionEventDto friendActionEventDto = FriendActionEventDto.builder()
                    .memberId(memberId)
                    .type(NotificationEventType.FRIEND_ACTION)
                    .friendMemberId(friend.getToMember().getId() == memberId ? friend.getFromMember().getId() : friend.getToMember().getId())
                    .status("DELETE")
                    .build();
            kafkaProducerService.sendNotificationEvent(friendActionEventDto);
        }

        friendRepository.deleteAllByFromMemberOrToMember(member, member);

        memberRepository.delete(member);

        // Kafka로 이벤트 발행
        MemberEventDto memberEventDto = MemberEventDto.builder()
                .memberId(memberId)
                .type("DELETE")
                .build();
        kafkaProducerService.sendMemberEvent(memberEventDto);



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
     * 멤버 정보 조회 (For Feign Client)
     */
    public MemberInfoResponseDto getMemberInfo(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new ErrorHandler(ErrorStatus.MEMBER_NOT_FOUND));
        // 개발용 로그
        log.info("[Member] Feign 에서 요청 받은 멤버 정보 = memberId = {}, name = {}, nickname = {}, email = {}, avatarUrl = {}, bannerUrl = {}, introduce = {}, globalStatus = {}",
                memberId, member.getName(), member.getNickname(), member.getEmail(), member.getAvatarUrl(), member.getBannerUrl(), member.getIntroduce(), member.getCustomPresenceStatus());
        return MemberInfoResponseDto.builder()
                .memberId(memberId)
                .name(member.getName())
                .nickname(member.getNickname())
                .email(member.getEmail())
                .avatarUrl(member.getAvatarUrl())
                .bannerUrl(member.getBannerUrl())
                .introduce(member.getIntroduce())
                .globalStatus(member.getCustomPresenceStatus())
                .build();
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
