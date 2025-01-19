package com.bbebig.chatserver.repository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class RedisSessionManager {

	public static final String ENTER_INFO = "SESSION_INFO"; // 연결된 클라이언트의 sessionId와 멤버 ID를 맵핑한 정보 저장
	private final RedisTemplate<String, Object> redisTemplate;
	private HashOperations<String, String, Long> enterInfoBySessionId;
	private HashOperations<String, Long, String> enterInfoByMemberId;

	@PostConstruct
	private void init() {
		enterInfoByMemberId = redisTemplate.opsForHash();
		enterInfoBySessionId = redisTemplate.opsForHash();
	}

	// 채팅방 입장 정보 저장
	public void saveConnectSessionInfoToRedis(String sessionId, Long memberId) {
		enterInfoBySessionId.put(ENTER_INFO, sessionId, memberId);
		enterInfoByMemberId.put(ENTER_INFO, memberId, sessionId);
	}

	// 채팅방 입장 정보 삭제
	public void deleteConnectSessionInfoToRedis(String sessionId, Long memberId) {
		enterInfoBySessionId.delete(ENTER_INFO, sessionId);
		enterInfoByMemberId.delete(ENTER_INFO, memberId);
	}

	// 세션 ID로 멤버 ID 조회
	public Long findMemberIdBySessionId(String sessionId) {
		return enterInfoBySessionId.get(ENTER_INFO, sessionId);
	}

	// 멤버 ID로 세션 ID 조회
	public String findSessionIdByMemberId(Long memberId) {
		return enterInfoByMemberId.get(ENTER_INFO, memberId);
	}

	// 멤버 ID 존재 여부 확인
	public boolean isExistMemberId(Long memberId) {
		return enterInfoByMemberId.hasKey(ENTER_INFO, memberId);
	}

}

