package com.bbebig.signalingserver.service.group;

import com.bbebig.signalingserver.domain.MessageType;
import com.bbebig.signalingserver.domain.Path;
import com.bbebig.signalingserver.domain.SignalMessage;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.IceCandidate;
import org.kurento.client.KurentoClient;
import org.kurento.client.MediaPipeline;
import org.kurento.client.WebRtcEndpoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KurentoManager {

    private final KurentoClient kurentoClient;
    private final SimpMessagingTemplate messagingTemplate;

    private final Set<String> registeredListenerEndpoints = Collections.newSetFromMap(new ConcurrentHashMap<>());

    // channelId -> MediaPipeline
    private final Map<String, MediaPipeline> pipelines = new ConcurrentHashMap<>();

    // channelId -> (memberId -> WebRtcEndpoint)
    private final Map<String, Map<String, WebRtcEndpoint>> endpoints = new ConcurrentHashMap<>();

    public KurentoManager(@Value("${kurento.ws.url}") String kurentoWsUrl, SimpMessagingTemplate messagingTemplate) {
        this.kurentoClient = KurentoClient.create(kurentoWsUrl);
        this.messagingTemplate = messagingTemplate;
    }

    /**
     * 채널에 해당하는 MediaPipeline 가져오고, 없으면 새로 생성
     */
    public synchronized MediaPipeline getOrCreatePipeline(String channelId) {
        if (!pipelines.containsKey(channelId)) {
            MediaPipeline newPipeline = kurentoClient.createMediaPipeline();
            pipelines.put(channelId, newPipeline);
        }
        return pipelines.get(channelId);
    }

    /**
     * 채널에 참여하는 사용자(memberId)용 WebRtcEndpoint 생성
     */
    public synchronized void createEndpoint(String channelId, String memberId) {
        log.info("[Signal] 채널 타입: Group, 채널 ID: {}, 사용자 ID: {}, 상세: Kurento WebRtcEndpoint 생성 시작", channelId, memberId);

        MediaPipeline pipeline = getOrCreatePipeline(channelId);
        log.info("[Signal] 채널 타입: Group, 채널 ID: {}, 기존 Kurento Pipeline 존재 여부: {}", channelId, pipelines.containsKey(channelId));

        WebRtcEndpoint webRtcEndpoint = new WebRtcEndpoint.Builder(pipeline).build();
        log.info("[Signal] 채널 타입: Group, 채널 ID: {}, 사용자 ID: {}, 상세: Kurento WebRtcEndpoint 생성 완료", channelId, memberId);

        // (1) 구글 STUN 설정
        webRtcEndpoint.setStunServerAddress("stun.l.google.com");
        webRtcEndpoint.setStunServerPort(19302);
        log.info("[Signal] 채널 타입: Group, 채널 ID: {}, 사용자 ID: {}, 상세: STUN 서버 설정 완료 ({}:{})",
                channelId, memberId, "stun.l.google.com", 19302);

        // (2) endpoints 저장
        endpoints.putIfAbsent(channelId, new ConcurrentHashMap<>());
        endpoints.get(channelId).put(memberId, webRtcEndpoint);
        log.info("[Signal] 채널 타입: Group, 채널 ID: {}, 사용자 ID: {}, 상세: Kurento WebRtcEndpoint 저장 완료", channelId, memberId);

        // ICE Candidate 리스너 등록
        addIceCandidateListener(channelId, memberId, webRtcEndpoint);

        log.info("[Signal] 채널 타입: Group, Kurento 현재 채널({})의 참가자 수: {}", channelId, endpoints.get(channelId).size());
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
                log.info("[Kurento] ICE Candidate 발견 - candidate: {}", kurentoCandidate.getCandidate());

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

    /**
     * 특정 사용자(memberId)의 WebRtcEndpoint 조회
     */
    public synchronized WebRtcEndpoint getEndpoint(String channelId, String memberId) {
        if (!endpoints.containsKey(channelId)) {
            return null;
        }
        return endpoints.get(channelId).get(memberId);
    }

    /**
     * 채널에서 해당 사용자의 WebRtcEndpoint 해제
     */
    public synchronized void removeEndpoint(String channelId, String memberId) {
        if (endpoints.containsKey(channelId)) {
            WebRtcEndpoint endpoint = endpoints.get(channelId).remove(memberId);
            // WebRtcEndpoint 리소스 해제
            if (endpoint != null) {
                endpoint.release();
            }
            // 채널이 비어있으면 제거
            if (endpoints.get(channelId).isEmpty()) {
                endpoints.remove(channelId);
            }
        }
    }

    /**
     * 채널 자체가 종료되면 모든 Endpoint와 Pipeline 정리
     */
    public synchronized void closeChannel(String channelId) {
        // 채널에 연결된 모든 WebRtcEndpoint 해제 및 제거
        if (endpoints.containsKey(channelId)) {
            endpoints.get(channelId).values().forEach(WebRtcEndpoint::release);
            endpoints.remove(channelId);
        }
        // 채널의 MediaPipeline 해제 및 제거
        if (pipelines.containsKey(channelId)) {
            pipelines.get(channelId).release();
            pipelines.remove(channelId);
        }
    }
}
