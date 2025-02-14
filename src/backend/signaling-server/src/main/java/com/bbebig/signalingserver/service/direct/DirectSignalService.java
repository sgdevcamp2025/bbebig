package com.bbebig.signalingserver.service.direct;

import com.bbebig.signalingserver.domain.Path;
import com.bbebig.signalingserver.domain.SignalMessage;
import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * 다이렉트 스트리밍 시그널링을 처리하는 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class DirectSignalService {

    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 다이렉트 시그널링 처리
     */
    public void processDirectSignal(SignalMessage message, String sessionId) {
        switch (message.getMessageType()) {
            case OFFER:
            case ANSWER:
            case CANDIDATE:
                handleDirectSignal(message, sessionId);
                break;
            default:
                log.error("[Stream] 채널 타입: Direct, 메시지 타입: {}, 상세: 지원되지 않는 메시지 타입입니다.", message.getMessageType());
                throw new ErrorHandler(ErrorStatus.GROUP_STREAM_INVALID_SIGNAL);
        }
    }

    /**
     * 다이렉트 offer/answer/candidate 교환
     */
    private void handleDirectSignal(SignalMessage message, String sessionId) {
        if (message.getReceiverId() == null) {
            log.error("[Stream] 채널 타입: Direct, 상세: receiverID가 누락되었습니다.");
            throw new ErrorHandler(ErrorStatus.DIRECT_STREAM_RECEIVER_ID_MISSING);
        }

        SignalMessage directMessage = SignalMessage.builder()
                .messageType(message.getMessageType())
                .senderId(sessionId)
                .receiverId(message.getReceiverId())
                .sdp(message.getSdp())
                .candidate(message.getCandidate())
                .build();

        messagingTemplate.convertAndSendToUser(
                message.getReceiverId(),
                Path.directSubPath,
                directMessage
        );
    }
}
