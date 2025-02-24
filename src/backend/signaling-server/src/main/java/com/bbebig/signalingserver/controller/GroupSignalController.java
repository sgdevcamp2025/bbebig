package com.bbebig.signalingserver.controller;

import com.bbebig.signalingserver.domain.SignalMessage;
import com.bbebig.signalingserver.service.group.strategy.MeshGroupSignalService;
import com.bbebig.signalingserver.service.group.strategy.SfuGroupSignalService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

/**
 * 그룹 스트리밍 시그널링을 처리하는 컨트롤러
 */
@Slf4j
@Controller
@RequiredArgsConstructor
public class GroupSignalController {

    private final SfuGroupSignalService sfuGroupSignalService;
    private final MeshGroupSignalService meshGroupSignalService;

    @MessageMapping("/group")
    public void onSfuGroupSignal(@Payload SignalMessage message,
                              @Header("simpSessionId") String sessionId) {

        log.info("[Signal] 채널 타입: Group - SFU, 메시지 타입: {}, 보낸 사람 유저 ID: {}, 세션 ID: {}, 채널 ID: {}",
                message.getMessageType(), message.getSenderId(), sessionId, message.getChannelId());

        sfuGroupSignalService.processGroupSignal(message);
    }

    @MessageMapping("/mesh-group")
    public void onMeshGroupSignal(@Payload SignalMessage message,
                              @Header("simpSessionId") String sessionId) {

        log.info("[Signal] 채널 타입: Group - Mesh, 메시지 타입: {}, 보낸 사람 유저 ID: {}, 세션 ID: {}, 채널 ID: {}",
                message.getMessageType(), message.getSenderId(), sessionId, message.getChannelId());

        meshGroupSignalService.processGroupSignal(message);
    }
}
