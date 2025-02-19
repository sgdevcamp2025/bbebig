package com.bbebig.signalingserver.service.group;

import com.bbebig.signalingserver.domain.MessageType;
import com.bbebig.signalingserver.domain.Path;
import com.bbebig.signalingserver.domain.SignalMessage;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.kurento.client.IceCandidate;
import org.kurento.client.KurentoClient;
import org.kurento.client.MediaPipeline;
import org.kurento.client.WebRtcEndpoint;
import org.kurento.client.WebRtcEndpoint.Builder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class KurentoManager {

    private final KurentoClient kurentoClient;
    private final SimpMessagingTemplate messagingTemplate;

    // channelId -> MediaPipeline
    private final Map<String, MediaPipeline> pipelines = new ConcurrentHashMap<>();

    // channelId -> (sessionId -> WebRtcEndpoint)
    private final Map<String, Map<String, WebRtcEndpoint>> endpoints = new ConcurrentHashMap<>();

    public KurentoManager(
            @Value("${kurento.ws.url}") String kurentoWsUrl,
            SimpMessagingTemplate messagingTemplate
    ) {
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
     * 채널에 참여하는 사용자(sessionId)용 WebRtcEndpoint 생성
     */
    public synchronized void createEndpoint(String channelId, String sessionId) {
        MediaPipeline pipeline = getOrCreatePipeline(channelId);
        WebRtcEndpoint webRtcEndpoint = new Builder(pipeline).build();

        // (1) 구글 STUN 설정
        webRtcEndpoint.setStunServerAddress("stun.l.google.com");
        webRtcEndpoint.setStunServerPort(19302);

        // (2) 서버 -> 클라이언트 ICE Candidate 전송을 위한 이벤트 리스너
        webRtcEndpoint.addOnIceCandidateListener(event -> {
            IceCandidate candidate = event.getCandidate();

            // SignalMessage 내부의 Candidate 클래스로 변환
            SignalMessage.Candidate candidateObj = SignalMessage.Candidate.builder()
                    .candidate(candidate.getCandidate())
                    .sdpMid(candidate.getSdpMid())
                    .sdpMLineIndex(candidate.getSdpMLineIndex())
                    .build();

            SignalMessage candidateMessage = SignalMessage.builder()
                    .messageType(MessageType.CANDIDATE)
                    .channelId(channelId)
                    .senderId("SFU_SERVER")
                    .receiverId(sessionId)
                    .candidate(candidateObj)
                    .build();

            messagingTemplate.convertAndSendToUser(
                    sessionId,
                    Path.directSubPath,
                    candidateMessage
            );
        });

        // (3) endpoints 저장
        endpoints.putIfAbsent(channelId, new ConcurrentHashMap<>());
        endpoints.get(channelId).put(sessionId, webRtcEndpoint);
    }

    /**
     * 특정 사용자(sessionId)의 WebRtcEndpoint 조회
     */
    public synchronized WebRtcEndpoint getEndpoint(String channelId, String sessionId) {
        if (!endpoints.containsKey(channelId)) {
            return null;
        }
        return endpoints.get(channelId).get(sessionId);
    }

    /**
     * 채널에서 해당 사용자의 WebRtcEndpoint 해제
     */
    public synchronized void removeEndpoint(String channelId, String sessionId) {
        if (endpoints.containsKey(channelId)) {
            WebRtcEndpoint endpoint = endpoints.get(channelId).remove(sessionId);
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
