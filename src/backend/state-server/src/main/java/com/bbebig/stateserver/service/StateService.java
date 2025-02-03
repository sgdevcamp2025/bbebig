package com.bbebig.stateserver.service;

import com.bbebig.stateserver.domain.MemberPresenceStatus;
import com.bbebig.stateserver.dto.StateResponseDto.*;
import com.bbebig.stateserver.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StateService {

	private final RedisRepository redisRepository;

	// 사용자 상태 확인
	public MemberStatusResponseDto checkMemberState(Long memberId) {
		log.info("[State] Check member state: {}", memberId);
		MemberPresenceStatus memberPresenceStatus = redisRepository.getMemberPresenceStatus(memberId);
		return MemberStatusResponseDto.builder()
				.memberId(memberId)
				.globalStatus(memberPresenceStatus.getGlobalStatus().toString())
				.actualStatus(memberPresenceStatus.getActualStatus().toString())
				.build();
	}
}
