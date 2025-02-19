package com.bbebig.signalingserver.service.group.strategy;

import com.bbebig.signalingserver.domain.Path;
import com.bbebig.signalingserver.domain.MessageType;
import com.bbebig.signalingserver.domain.SignalMessage;
import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import com.bbebig.signalingserver.service.group.ChannelManager;
import com.bbebig.signalingserver.service.group.KurentoManager;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.IceCandidate;
import org.kurento.client.WebRtcEndpoint;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

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

    /**
     * 그룹 시그널링 처리
     */
    @Override
    public void processGroupSignal(SignalMessage message, String sessionId) {
        switch (message.getMessageType()) {
            case JOIN_CHANNEL:
                handleJoinChannel(message, sessionId);
                break;
            case LEAVE_CHANNEL:
                handleLeaveChannel(message, sessionId);
                break;
            case OFFER:
                handleOffer(message, sessionId);
                // OFFER 이후 바로 CANDIDATE가 들어올 수 있으므로 break 없이 진행
            case CANDIDATE:
                handleCandidate(message, sessionId);
                break;
            case ANSWER:
                // SFU 입장에선 브라우저->서버 ANSWER는 일반적으로 사용하지 않으므로 처리 X
                break;
            default:
                log.error("[Signal] 채널 타입: Group, 메시지 타입: {}, 상세: 지원되지 않는 메시지 타입입니다.", message.getMessageType());
                throw new ErrorHandler(ErrorStatus.GROUP_STREAM_INVALID_SIGNAL);
        }
    }

    /**
     * 채널 입장
     */
    private void handleJoinChannel(SignalMessage message, String sessionId) {
        boolean joinStatus = channelManager.joinChannel(message.getChannelId(), sessionId);

        // 최대 인원 초과로 인해 join 실패
        if (!joinStatus) {
            SignalMessage fullMessage = SignalMessage.builder()
                    .messageType(MessageType.CHANNEL_FULL)
                    .channelId(message.getChannelId())
                    .senderId(sessionId)
                    .build();

            messagingTemplate.convertAndSendToUser(
                    sessionId,
                    Path.directSubPath,
                    fullMessage
            );
        }

        // (1) KurentoManager에서 WebRtcEndpoint 생성
        kurentoManager.createEndpoint(message.getChannelId(), sessionId);

//        // 나를 제외한 기존 멤버 목록
//        Set<String> participants = channelManager.getParticipants(message.getChannelId());
//        List<String> otherUsers = new ArrayList<>(participants);
//        otherUsers.remove(sessionId);

        // (2) 본인에게 기존 멤버 정보 안내 (필요 시 ID 목록 추가)
        notifyExistUsers(message, sessionId);

        // (3) 방 전체에 새 사용자의 입장 알림
        notifyUserJoined(message, sessionId);
    }

    /**
     * 본인에게 기존 멤버 정보를 전달
     */
    private void notifyExistUsers(SignalMessage message, String sessionId) {
        // TODO: 기존 유저 ID 추가
        SignalMessage allUsersMessage = SignalMessage.builder()
                .messageType(MessageType.EXIST_USERS)
                .channelId(message.getChannelId())
                .senderId(sessionId)
                .build();

        messagingTemplate.convertAndSendToUser(
                sessionId,
                Path.directSubPath,
                allUsersMessage
        );
    }

    /**
     * 방 전체에 새로운 사용자의 입장을 알림
     */
    private void notifyUserJoined(SignalMessage message, String sessionId) {
        SignalMessage userJoinedMessage = SignalMessage.builder()
                .messageType(MessageType.USER_JOINED)
                .channelId(message.getChannelId())
                .senderId(sessionId)
                .build();

        messagingTemplate.convertAndSend(
                Path.groupSubPath + message.getChannelId(),
                userJoinedMessage
        );
    }

    /**
     * 채널 퇴장
     */
    private void handleLeaveChannel(SignalMessage message, String sessionId) {
        // kurentoManager에서 endpoint 해제
        kurentoManager.removeEndpoint(message.getChannelId(), sessionId);
        channelManager.leaveChannel(sessionId);

        // 채널 내 인원이 0이면 채널 종료
        if (channelManager.getParticipants(message.getChannelId()).isEmpty()) {
            kurentoManager.closeChannel(message.getChannelId());
        }

        SignalMessage userLeftMessage = SignalMessage.builder()
                .messageType(MessageType.USER_LEFT)
                .channelId(message.getChannelId())
                .senderId(sessionId)
                .build();

        messagingTemplate.convertAndSend(
                Path.groupSubPath + message.getChannelId(),
                userLeftMessage
        );
    }

    /**
     * 브라우저에서 온 SDP Offer -> Kurento로 넘기고 -> SDP Answer 만들어서 브라우저에게 전달
     */
    private void handleOffer(SignalMessage message, String sessionId) {
        // 1) WebRtcEndpoint 조회
        WebRtcEndpoint endpoint = kurentoManager.getEndpoint(message.getChannelId(), sessionId);
        if (endpoint == null) {
            log.error("[Signal] 채널 타입: Group, 채널 ID: {}, 세션 ID: {}, 상세: Offer 처리 실패 - 엔드포인트를 찾을 수 없습니다.",
                    message.getChannelId(), sessionId);
            throw new ErrorHandler(ErrorStatus.GROUP_STREAM_ENDPOINT_NOT_FOUND);
        }

        // 2) 브라우저에서 전달한 Offer SDP 추출
        if (message.getSdp() == null || message.getSdp().getSdp() == null) {
            log.error("[Signal] 채널 타입: Group, 세션 ID: {}, 상세: Offer 처리 실패 - Sdp가 null입니다.", sessionId);
            return;
        }
        String offerSdp = message.getSdp().getSdp();

        // 3) Kurento에 Offer 적용 -> Answer 생성
        //    (주의: 일반적으로 client "offer" => server calls processOffer(offerSdp))
        String sdpAnswer = endpoint.processOffer(offerSdp);

        // ICE Candidate 수집 시작
        endpoint.gatherCandidates();

        // 5) 브라우저로 ANSWER 메시지 전송
        SignalMessage answerMessage = SignalMessage.builder()
                .messageType(MessageType.ANSWER)
                .channelId(message.getChannelId())
                .senderId("SFU_SERVER")
                .receiverId(sessionId)
                .sdp(SignalMessage.Sdp.builder()
                        .type("answer")
                        .sdp(sdpAnswer)
                        .build())
                .build();

        messagingTemplate.convertAndSendToUser(
                sessionId,
                Path.directSubPath,
                answerMessage
        );
    }

    /**
     * 브라우저가 보낸 ICE candidate -> Kurento endpoint에 등록
     */
    private void handleCandidate(SignalMessage message, String sessionId) {
        WebRtcEndpoint endpoint = kurentoManager.getEndpoint(message.getChannelId(), sessionId);
        if (endpoint == null) {
            log.error("[Signal] 채널 타입: Group, 체널 ID: {}, 세션 ID: {}, 상세: Candidate 처리 실패 - 엔드포인트를 찾을 수 없습니다.",
                    message.getCandidate(), sessionId);
            return;
        }

        // 브라우저가 보낸 Candidate 필드
        SignalMessage.Candidate candidateObj = message.getCandidate();
        if (candidateObj == null || candidateObj.getCandidate() == null) {
            log.error("[Signal]] 채널 타입: Group, 세션: {}, 상세: Candidate 처리 실패 - candidate 객체가 null입니다.", sessionId);
            return;
        }

        // Kurento IceCandidate로 변환
        String candidate = candidateObj.getCandidate();
        String sdpMid = candidateObj.getSdpMid();
        Integer sdpMLineIndex = candidateObj.getSdpMLineIndex();

        IceCandidate kc = new IceCandidate(candidate, sdpMid, sdpMLineIndex);
        endpoint.addIceCandidate(kc);

        log.info("[Signal] 채널 타입: Group, 세션 ID: {}, 채널 ID: {}, candidate: {}, 상세: ICE Candidate 등록 완료",
                sessionId, message.getChannelId(), candidate);

    }
}
