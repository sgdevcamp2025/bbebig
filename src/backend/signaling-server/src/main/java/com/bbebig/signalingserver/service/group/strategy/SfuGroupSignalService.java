package com.bbebig.signalingserver.service.group.strategy;

import com.bbebig.signalingserver.domain.Path;
import com.bbebig.signalingserver.domain.MessageType;
import com.bbebig.signalingserver.domain.SignalMessage;
import com.bbebig.signalingserver.global.response.code.error.ErrorStatus;
import com.bbebig.signalingserver.global.response.exception.ErrorHandler;
import com.bbebig.signalingserver.service.group.ChannelManager;
import com.bbebig.signalingserver.service.group.KurentoManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
            case CANDIDATE:
                handleCandidate(message, sessionId);
                break;
            case ANSWER:
                break;
            default:
                log.error("[Stream] 채널 타입: Group, 메시지 타입: {}, 상세: 지원되지 않는 메시지 타입입니다.", message.getMessageType());
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
            SignalMessage fullMessage = SignalMessage.builder().
                    messageType(MessageType.CHANNEL_FULL)
                    .senderId(sessionId)
                    .build();

            messagingTemplate.convertAndSendToUser(
                    sessionId,
                    Path.directSubPath,
                    fullMessage
            );
        }

        // KurentoManager에서 WebRtcEndpoint 생성
        kurentoManager.createEndpoint(message.getChannelId(), sessionId);

//        // 나를 제외한 기존 멤버 목록
//        Set<String> participants = channelManager.getParticipants(message.getChannelId());
//        List<String> otherUsers = new ArrayList<>(participants);
//        otherUsers.remove(sessionId);

        notifyExistUsers(message, sessionId);
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

        // 남아있는 인원이 0이면 채널 close
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
        WebRtcEndpoint endpoint = kurentoManager.getEndpoint(message.getChannelId(), sessionId);
        if (endpoint == null) {
            log.error("[Stream] 채널 타입: Group, 채널 ID: {}, 세션 ID: {}, 상세: Offer 처리 실패 - 엔드포인트를 찾을 수 없습니다.",
                    message.getChannelId(), sessionId);
            throw new ErrorHandler(ErrorStatus.GROUP_STREAM_ENDPOINT_NOT_FOUND);
        }

        // TODO: JSON -> String 형태로 파싱
        // Kurento processOffer -> return SDP Answer
        String sdpOffer = (String) message.getSdp(); // 브라우저에서 보내온 SDP
        String sdpAnswer = endpoint.processAnswer(sdpOffer);

        // ICE Candidate 수집 시작
        endpoint.gatherCandidates();

        SignalMessage answerMessage = SignalMessage.builder()
                .messageType(MessageType.ANSWER)
                .channelId(message.getChannelId())
                .senderId("SFU_SERVER")
                .receiverId(sessionId)
                .sdp(sdpAnswer)
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
            log.error("[Stream] 채널 타입: Group, 체널 ID: {}, 세션 ID: {}, 상세: Candidate 처리 실패 - 엔드포인트를 찾을 수 없습니다.",
                    message.getCandidate(), sessionId);
            return;
        }

        // TODO: JSON -> String 형태로 파싱
        Object candidate = message.getCandidate();
//        String sdpMid = message.getSdpMid();
//        int sdpMLineIndex = message.getSdpMLineIndex();
//
//        IceCandidate candidate = new IceCandidate(candidate, sdpMid, sdpMLineIndex);
//        endpoint.addIceCandidate(candidate);
    }
}
