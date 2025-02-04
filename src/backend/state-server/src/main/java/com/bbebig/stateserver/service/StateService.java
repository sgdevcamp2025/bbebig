package com.bbebig.stateserver.service;

import com.bbebig.commonmodule.redis.domain.MemberPresenceStatus;
import com.bbebig.stateserver.dto.StateResponseDto.*;
import com.bbebig.stateserver.repository.MemberRedisRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class StateService {

	private final MemberRedisRepositoryImpl memberRedisRepositoryImpl;

	// 사용자 상태 확인
	public MemberStatusResponseDto checkMemberState(Long memberId) {
		log.info("[State] Check member state: {}", memberId);
		MemberPresenceStatus memberPresenceStatus = memberRedisRepositoryImpl.getMemberPresenceStatus(memberId);
		if (memberPresenceStatus == null) {
			// TODO : 추후 예외 처리 로직 추가
			log.error("[State] StateService: 사용자 상태 정보 없음. memberId: {}", memberId);
			return null;
		}
		return MemberStatusResponseDto.builder()
				.memberId(memberId)
				.globalStatus(memberPresenceStatus.getGlobalStatus().toString())
				.actualStatus(memberPresenceStatus.getActualStatus().toString())
				.build();
	}


}
