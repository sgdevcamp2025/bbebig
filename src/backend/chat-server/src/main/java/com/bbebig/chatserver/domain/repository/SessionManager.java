package com.bbebig.chatserver.domain.repository;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.ConcurrentHashMap;


@Slf4j
@Transactional
@RequiredArgsConstructor
@Service
public class SessionManager {

	// ConcurrentHashMap으로 세션 정보 저장
	private final ConcurrentHashMap<String, Long> sessionToMemberMap = new ConcurrentHashMap<>();
	private final ConcurrentHashMap<Long, String> memberToSessionMap = new ConcurrentHashMap<>();

	// 채팅방 입장 정보 저장
	public void saveConnectSessionInfo(String sessionId, Long memberId) {
		sessionToMemberMap.put(sessionId, memberId);
		memberToSessionMap.put(memberId, sessionId);
	}

	// 채팅방 입장 정보 삭제
	public void deleteConnectSessionInfo(String sessionId, Long memberId) {
		sessionToMemberMap.remove(sessionId);
		memberToSessionMap.remove(memberId);
	}

	// 세션 ID로 멤버 ID 조회
	public Long findMemberIdBySessionId(String sessionId) {
		return sessionToMemberMap.get(sessionId);
	}

	// 멤버 ID로 세션 ID 조회
	public String findSessionIdByMemberId(Long memberId) {
		return memberToSessionMap.get(memberId);
	}

	// 멤버 ID 존재 여부 확인
	public boolean isExistMemberId(Long memberId) {
		return memberToSessionMap.containsKey(memberId);
	}

	// 세션 ID 존재 여부 확인
	public boolean isExistSessionId(String sessionId) {
		return sessionToMemberMap.containsKey(sessionId);
	}
}

