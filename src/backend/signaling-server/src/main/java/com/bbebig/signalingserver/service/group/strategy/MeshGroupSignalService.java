package com.bbebig.signalingserver.service.group.strategy;

import com.bbebig.signalingserver.domain.Path;
import com.bbebig.signalingserver.domain.SignalMessage;
import com.bbebig.signalingserver.domain.MessageType;
import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import com.bbebig.signalingserver.service.group.ChannelManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;

/**
 * 그룹 스트리밍 시그널링을 처리하는 서비스 (Mesh)
 */
@Slf4j
//@Service
@RequiredArgsConstructor
public class MeshGroupSignalService implements GroupSignalStrategy {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChannelManager channelManager;

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
            case ANSWER:
            case CANDIDATE:
                handleGroupSignal(message, sessionId);
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
        boolean joinStatus = channelManager.joinChannel(message.getChannelId(), message.getSenderId());

        // 최대 인원 초과로 인해 join 실패
        if (!joinStatus) {
            SignalMessage fullMessage = SignalMessage.builder().
                    messageType(MessageType.CHANNEL_FULL)
                    .senderId(message.getSenderId())
                    .build();

            messagingTemplate.convertAndSendToUser(
                    sessionId,
                    Path.directSubPath,
                    fullMessage
            );
        }

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
                .senderId(message.getSenderId())
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
                .senderId(message.getSenderId())
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
        channelManager.leaveChannel(message.getSenderId());

        SignalMessage userLeftMessage = SignalMessage.builder()
                .messageType(MessageType.USER_LEFT)
                .channelId(message.getChannelId())
                .senderId(message.getSenderId())
                .build();

        messagingTemplate.convertAndSend(
                Path.groupSubPath + message.getChannelId(),
                userLeftMessage
        );
    }

    /**
     * 그룹 offer/answer/candidate 전달
     */
    private void handleGroupSignal(SignalMessage message, String sessionId) {
        String channelId = channelManager.getChannelIdByMemberId(message.getSenderId());

        if (channelId == null) {
            log.error("[Signal] 채널 타입: Group, 상세: 채널 ID가 누락되었습니다.");
            throw new ErrorHandler(ErrorStatus.GROUP_STREAM_SESSION_NOT_FOUND);
        }

        SignalMessage groupMessage = SignalMessage.builder()
                .messageType(message.getMessageType())
                .senderId(message.getSenderId())
                .sdp(message.getSdp())
                .candidate(message.getCandidate())
                .build();

        messagingTemplate.convertAndSend(
                Path.groupSubPath + channelId,
                groupMessage
        );
    }
}
