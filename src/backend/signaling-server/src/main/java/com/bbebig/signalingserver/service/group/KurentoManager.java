package com.bbebig.signalingserver.service.group;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.kurento.client.KurentoClient;
import org.kurento.client.MediaPipeline;
import org.kurento.client.WebRtcEndpoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KurentoManager {

    private final KurentoClient kurentoClient;

    // channelId -> MediaPipeline
    private final Map<String, MediaPipeline> pipelines = new ConcurrentHashMap<>();

    // channelId -> (memberId -> WebRtcEndpoint)
    private final Map<String, Map<String, WebRtcEndpoint>> endpoints = new ConcurrentHashMap<>();

    public KurentoManager(@Value("${kurento.ws.url}") String kurentoWsUrl) {
        this.kurentoClient = KurentoClient.create(kurentoWsUrl);
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
        log.info("[Kurento] 채널 ID: {}, 사용자 ID: {}, 상세: Kurento WebRtcEndpoint 생성 시작", channelId, memberId);

        MediaPipeline pipeline = getOrCreatePipeline(channelId);
        log.info("[Kurento] 채널 ID: {}, 기존 Kurento Pipeline 존재 여부: {}", channelId, pipelines.containsKey(channelId));

        WebRtcEndpoint webRtcEndpoint = new WebRtcEndpoint.Builder(pipeline).build();
        log.info("[Kurento] 채널 ID: {}, 사용자 ID: {}, 상세: Kurento WebRtcEndpoint 생성 완료", channelId, memberId);

        // (1) 구글 STUN 설정
        webRtcEndpoint.setStunServerAddress("stun.l.google.com");
        webRtcEndpoint.setStunServerPort(19302);
        log.info("[Kurento] 채널 ID: {}, 사용자 ID: {}, 상세: STUN 서버 설정 완료 ({}:{})",
                channelId, memberId, "stun.l.google.com", 19302);

        // (2) endpoints 저장
        endpoints.putIfAbsent(channelId, new ConcurrentHashMap<>());
        endpoints.get(channelId).put(memberId, webRtcEndpoint);
        log.info("[Kurento] 채널 ID: {}, 사용자 ID: {}, 상세: Kurento WebRtcEndpoint 저장 완료", channelId, memberId);

        log.info("[Kurento] Kurento 현재 채널({})의 참가자 수: {}", channelId, endpoints.get(channelId).size());
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
