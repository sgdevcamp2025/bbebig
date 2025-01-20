package com.bbebig.signalingserver.service.group;

import com.bbebig.signalingserver.global.response.code.error.ErrorStatus;
import com.bbebig.signalingserver.global.response.exception.ErrorHandler;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChannelManager {

    // 활성화된 방과 속해있는 유저 목록 (channelId -> sessionId의 리스트)
    private final Map<String, Set<String>> channelParticipantsMap = new ConcurrentHashMap<>();

    // 특정 유저가 속한 방 (sessionId -> channelId)
    private final Map<String, String> sessionChannelMap = new ConcurrentHashMap<>();

    private static final int MAX_CHANNEL_CAPACITY = 5;

    /**
     * 채널 입장
     */
    public synchronized boolean joinChannel(String channelId, String sessionId) {
        channelParticipantsMap.putIfAbsent(channelId, new HashSet<>());

        // 인원 초과 시 Reject
        if (channelParticipantsMap.get(channelId).size() >= MAX_CHANNEL_CAPACITY) {
            return false;
        }

        channelParticipantsMap.get(channelId).add(sessionId);
        sessionChannelMap.put(sessionId, channelId);
        return true;
    }

    /**
     * 채널 퇴장
     */
    public synchronized void leaveChannel(String sessionId) {
        String channelId = sessionChannelMap.get(sessionId);

        if (channelId == null) {
            log.error("[Stream] 채널 타입: Group, 세션 ID: {}, 상세: 채널 ID가 존재하지 않습니다.", sessionId);
            throw new ErrorHandler(ErrorStatus.GROUP_STREAM_CHANNEL_NOT_FOUND);
        }

        channelParticipantsMap.get(channelId).remove(sessionId);
        if (channelParticipantsMap.get(channelId).isEmpty()) {
            channelParticipantsMap.remove(channelId);
        }
        sessionChannelMap.remove(sessionId);
    }

    public String getChannelIdBySessionId(String sessionId) {
        return sessionChannelMap.get(sessionId);
    }

    public Set<String> getParticipants(String channelId) {
        return channelParticipantsMap.getOrDefault(channelId, Set.of());
    }
}
