package com.bbebig.signalingserver.controller;

import com.bbebig.signalingserver.domain.SignalMessage;
import com.bbebig.signalingserver.service.direct.DirectSignalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

/**
 * 다이렉트 스트리밍 시그널링을 처리하는 컨트롤러
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class DirectSignalController {

    private final DirectSignalService directSignalService;

    @MessageMapping("/direct")
    public void onDirectSignal(@Payload SignalMessage message,
                               @Header("simpSessionId") String sessionId) {

        log.info("[Stream] 채널 타입: Direct, 메시지 타입: {}, 보낸 사람 ID: {}, 받는 사람 ID: {}",
                message.getMessageType(), sessionId, message.getReceiverId());

        directSignalService.processDirectSignal(message, sessionId);
    }
}
