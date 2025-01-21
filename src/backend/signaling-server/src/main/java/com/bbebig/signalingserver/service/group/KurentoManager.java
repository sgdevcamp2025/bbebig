package com.bbebig.signalingserver.service.group;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.kurento.client.KurentoClient;
import org.kurento.client.MediaPipeline;
import org.kurento.client.WebRtcEndpoint;
import org.kurento.client.WebRtcEndpoint.Builder;
import org.springframework.stereotype.Component;

@Component
public class KurentoManager {

    private final KurentoClient kurentoClient;

    // channelId -> MediaPipeline
    private final Map<String, MediaPipeline> pipelines = new ConcurrentHashMap<>();

    // channelId -> (sessionId -> WebRtcEndpoint)
    private final Map<String, Map<String, WebRtcEndpoint>> endpoints = new ConcurrentHashMap<>();

    // TODO: kurento-media-server 주소 변경
    public KurentoManager() {
        this.kurentoClient = KurentoClient.create("ws://localhost:8888/kurento");
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
