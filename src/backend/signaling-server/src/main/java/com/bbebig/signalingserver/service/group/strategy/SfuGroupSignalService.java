package com.bbebig.signalingserver.service.group.strategy;

import com.bbebig.signalingserver.domain.Path;
import com.bbebig.signalingserver.domain.MessageType;
import com.bbebig.signalingserver.domain.SignalMessage;
import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import com.bbebig.signalingserver.service.group.ChannelManager;
import com.bbebig.signalingserver.service.group.KurentoManager;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.IceCandidate;
import org.kurento.client.WebRtcEndpoint;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 그룹 스트리밍 시그널링을 처리하는 서비스 (SFU)
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SfuGroupSignalService implements GroupSignalStrategy {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChannelManager channelManager;
    private final KurentoManager kurentoManager;

    private final Set<String> registeredListenerEndpoints = Collections.newSetFromMap(new ConcurrentHashMap<>());

    /**
     * 그룹 시그널링 처리
     */
    @Override
    public void processGroupSignal(SignalMessage message) {
        switch (message.getMessageType()) {
            case JOIN_CHANNEL:
                handleJoinChannel(message);
                break;
            case LEAVE_CHANNEL:
                handleLeaveChannel(message);
                break;
            case OFFER:
                handleOffer(message);
                break;
            case CANDIDATE:
                handleCandidate(message);
                break;
            default:
                log.error("[Signal] 채널 타입: Group, 메시지 타입: {}, 상세: 지원되지 않는 메시지 타입입니다.", message.getMessageType());
                throw new ErrorHandler(ErrorStatus.GROUP_STREAM_INVALID_SIGNAL);
        }
    }

    /**
     * 채널 입장
     */
    private void handleJoinChannel(SignalMessage message) {
        String memberId = message.getSenderId();
        String channelId = message.getChannelId();

        boolean joinStatus = channelManager.joinChannel(channelId, memberId);

        // 최대 인원 초과로 인해 join 실패
        if (!joinStatus) {
            SignalMessage fullMessage = SignalMessage.builder()
                    .messageType(MessageType.CHANNEL_FULL)
                    .channelId(channelId)
                    .senderId(memberId)
                    .build();

            messagingTemplate.convertAndSend(
                    Path.directSubPath + memberId,
                    fullMessage
            );
        }

        // (1) KurentoManager에서 WebRtcEndpoint 생성
        kurentoManager.createEndpoint(channelId, memberId);

        // JOIN_CHANNEL 시점에 ICE Candidate 리스너를 등록하고 후보 수집 시작
        WebRtcEndpoint endpoint = kurentoManager.getEndpoint(channelId, memberId);
        addIceCandidateListener(channelId, memberId, endpoint);

        // ICE Candidate 수집 시작
        endpoint.gatherCandidates();
        log.info("[Kurento] ICE Candidate gathering 시작 - 채널: {}, 유저 ID: {}", channelId, memberId);

        // 기존 멤버 목록
        Set<String> participants = channelManager.getParticipants(message.getChannelId());

        // (2) 본인에게 기존 멤버 정보 안내 (필요 시 ID 목록 추가)
        notifyExistUsers(message, participants);

        // (3) 방 전체에 새 사용자의 입장 알림
        notifyUserJoined(message);
    }

    /**
     * 본인에게 기존 멤버 정보를 전달
     */
    private void notifyExistUsers(SignalMessage message, Set<String> participants) {
        SignalMessage allUsersMessage = SignalMessage.builder()
                .messageType(MessageType.EXIST_USERS)
                .channelId(message.getChannelId())
                .senderId(message.getSenderId())
                .participants(participants.stream()
                        .map(Long::parseLong)
                        .toList())
                .build();

        messagingTemplate.convertAndSend(
                Path.directSubPath + message.getSenderId(),
                allUsersMessage
        );

        log.info("[Signal] 채널 타입: Group, 대상 경로: {}, 상세: EXIST_USERS 메시지 전송",
                Path.directSubPath + message.getSenderId());
    }

    /**
     * 방 전체에 새로운 사용자의 입장을 알림
     */
    private void notifyUserJoined(SignalMessage message) {
        SignalMessage userJoinedMessage = SignalMessage.builder()
                .messageType(MessageType.USER_JOINED)
                .channelId(message.getChannelId())
                .senderId(message.getSenderId())
                .build();

        messagingTemplate.convertAndSend(
                Path.groupSubPath + message.getChannelId(),
                userJoinedMessage
        );

        log.info("[Signal] 채널 타입: Group, 대상 경로: {}, 상세: USER_JOINED 메시지 전송",
                Path.groupSubPath + message.getChannelId());
    }

    /**
     * 채널 퇴장
     */
    private void handleLeaveChannel(SignalMessage message) {
        String memberId = message.getSenderId();
        String channelId = message.getChannelId();

        // kurentoManager에서 endpoint 해제
        kurentoManager.removeEndpoint(channelId, memberId);
        channelManager.leaveChannel(memberId);

        // 채널 내 인원이 0이면 채널 종료
        if (channelManager.getParticipants(channelId).isEmpty()) {
            kurentoManager.closeChannel(channelId);
        }

        SignalMessage userLeftMessage = SignalMessage.builder()
                .messageType(MessageType.USER_LEFT)
                .channelId(channelId)
                .senderId(memberId)
                .build();

        messagingTemplate.convertAndSend(
                Path.groupSubPath + channelId,
                userLeftMessage
        );
    }

    /**
     * 브라우저에서 온 SDP Offer -> Kurento로 넘기고 -> SDP Answer 만들어서 브라우저에게 전달
     */
    private void handleOffer(SignalMessage message) {
        String memberId = message.getSenderId();
        String channelId = message.getChannelId();

        // 1) WebRtcEndpoint 조회
        WebRtcEndpoint endpoint = kurentoManager.getEndpoint(channelId, memberId);
        if (endpoint == null) {
            log.error("[Signal] 채널 타입: Group, 채널 ID: {}, 유저 ID: {}, 상세: Offer 처리 실패 - 엔드포인트를 찾을 수 없습니다.",
                    channelId, memberId);
            throw new ErrorHandler(ErrorStatus.GROUP_STREAM_ENDPOINT_NOT_FOUND);
        }

        // 2) 브라우저에서 전달한 Offer SDP 추출
        if (message.getSdp() == null || message.getSdp().getSdp() == null) {
            log.error("[Signal] 채널 타입: Group, 유저 ID: {}, 상세: Offer 처리 실패 - Sdp가 null입니다.", memberId);
            return;
        }
        String offerSdp = message.getSdp().getSdp();

        // 3) Kurento에 Offer 적용 -> Answer 생성
        String AnswerSdp = endpoint.processOffer(offerSdp);

        // 4) 브라우저로 ANSWER 메시지 전송
        SignalMessage answerMessage = SignalMessage.builder()
                .messageType(MessageType.ANSWER)
                .channelId(channelId)
                .senderId("SFU_SERVER")
                .receiverId(memberId)
                .sdp(SignalMessage.Sdp.builder()
                        .type("answer")
                        .sdp(AnswerSdp)
                        .build())
                .build();

        messagingTemplate.convertAndSend(
                Path.directSubPath + memberId,
                answerMessage
        );

        log.info("[Signal] 채널 타입: Group, 유저 ID: {}, 채널 ID: {}, 상세: ANSWER 전송 완료",
                memberId, channelId);
    }

    /**
     * 브라우저가 보낸 ICE candidate -> Kurento endpoint에 등록
     */
    private void handleCandidate(SignalMessage message) {
        String memberId = message.getSenderId();
        String channelId = message.getChannelId();

        WebRtcEndpoint endpoint = kurentoManager.getEndpoint(channelId, memberId);
        if (endpoint == null) {
            log.error("[Signal] 채널 타입: Group, 체널 ID: {}, 유저 ID: {}, 상세: Candidate 처리 실패 - 엔드포인트를 찾을 수 없습니다.",
                    channelId, memberId);
            return;
        }

        // 브라우저가 보낸 Candidate 필드
        SignalMessage.Candidate candidate = message.getCandidate();
        if (candidate == null || candidate.getCandidate() == null) {
            log.error("[Signal] 채널 타입: Group, 유저 ID: {}, 상세: Candidate 처리 실패 - candidate 객체가 null입니다.",
                    memberId);
            return;
        }
        if (candidate.getSdpMid() == null) {
            log.error("[Signal] 채널 타입: Group, 유저 ID: {}, 상세: Candidate 처리 실패 - sdpMid가 null입니다.",
                    memberId);
            return;
        }
        if (candidate.getSdpMLineIndex() == null) {
            log.error("[Signal] 채널 타입: Group, 유저 ID: {}, 상세: Candidate 처리 실패 - sdpMLineIndex가 null입니다.",
                    memberId);
            return;
        }

        // Kurento IceCandidate로 변환
        IceCandidate kc = new IceCandidate(
                candidate.getCandidate(),
                candidate.getSdpMid(),
                candidate.getSdpMLineIndex()
        );
        endpoint.addIceCandidate(kc);

        log.info("[Signal] 채널 타입: Group, 유저 ID: {}, 채널 ID: {}, candidate: {}, 상세: ICE Candidate 등록 완료",
                memberId, channelId, candidate.getCandidate());
    }

    /**
     * ICE Candidate 리스너 등록
     */
    private void addIceCandidateListener(String channelId, String memberId, WebRtcEndpoint endpoint) {
        String endpointKey = channelId + "_" + memberId;
        if (!registeredListenerEndpoints.contains(endpointKey)) {
            log.info("[Kurento] ICE Candidate Listener 등록 - 채널: {}, 유저: {}", channelId, memberId);

            endpoint.addIceCandidateFoundListener(event -> {
                IceCandidate kurentoCandidate = event.getCandidate();
                log.info("[Kurento] ICE Candidate 발견 - candidate: {}, sdpMid: {}, sdpMLineIndex: {}",
                        kurentoCandidate.getCandidate(), kurentoCandidate.getSdpMid(),
                        kurentoCandidate.getSdpMLineIndex());

                // Candidate 정보를 담은 SignalMessage 생성
                SignalMessage candidateMessage = SignalMessage.builder()
                        .messageType(MessageType.CANDIDATE) // CANDIDATE 메시지 타입 설정
                        .channelId(channelId)
                        .senderId("SFU_SERVER") // 서버가 보낸 메시지로 표시
                        .receiverId(memberId)   // 클라이언트에게 전달
                        .candidate(SignalMessage.Candidate.builder()
                                .candidate(kurentoCandidate.getCandidate())
                                .sdpMid(kurentoCandidate.getSdpMid())
                                .sdpMLineIndex(kurentoCandidate.getSdpMLineIndex())
                                .build())
                        .build();

                // WebSocket을 통해 클라이언트로 전송
                messagingTemplate.convertAndSend(
                        Path.directSubPath + memberId,
                        candidateMessage
                );

                log.info("[Signal] 채널 타입: Group, 유저 ID: {}, candidate: {}, 상세: Kurento에서 ICE Candidate 전송 완료",
                        memberId, kurentoCandidate.getCandidate());
            });

            // 리스너 등록 완료 후 해당 endpoint 키를 등록
            registeredListenerEndpoints.add(endpointKey);
        }
    }
}
