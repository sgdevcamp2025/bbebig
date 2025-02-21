package com.bbebig.stateserver.service;

import com.bbebig.commonmodule.clientDto.StateFeignResponseDto.*;
import com.bbebig.commonmodule.clientDto.UserFeignResponseDto.*;
import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import com.bbebig.commonmodule.kafka.dto.PresenceEventDto;
import com.bbebig.commonmodule.kafka.dto.model.PresenceType;
import com.bbebig.commonmodule.redis.domain.MemberPresenceStatus;
import com.bbebig.commonmodule.redis.domain.ServerMemberStatus;
import com.bbebig.stateserver.client.MemberClient;
import com.bbebig.stateserver.client.ServiceClient;
import com.bbebig.stateserver.repository.DmRedisRepositoryImpl;
import com.bbebig.stateserver.repository.MemberRedisRepositoryImpl;
import com.bbebig.stateserver.repository.ServerRedisRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

import static com.bbebig.commonmodule.clientDto.ServiceFeignResponseDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class StateService {

	private final KafkaProducerService kafkaProducerService;

	private final MemberRedisRepositoryImpl memberRedisRepositoryImpl;
	private final ServerRedisRepositoryImpl serverRedisRepositoryImpl;
	private final DmRedisRepositoryImpl dmRedisRepositoryImpl;

	private final ServiceClient serviceClient;
	private final MemberClient memberClient;

	// 사용자 상태 확인
	// GET /state/member/{memberId}
	public MemberPresenceResponseDto checkMemberState(Long memberId) {
		MemberPresenceStatus memberPresenceStatus = memberRedisRepositoryImpl.getMemberPresenceStatus(memberId);
		log.info("[State] StateService: 사용자 상태 확인. memberId: {}, memberPresenceStatus: {}", memberId, memberPresenceStatus);
		if (memberPresenceStatus == null) {
			MemberCustomStatusResponseDto memberGlobalStatus = memberClient.getMemberCustomStatus(memberId);
			if (memberGlobalStatus == null) {
				throw new ErrorHandler(ErrorStatus.MEMBER_GLOBAL_STATE_NOT_FOUND);
			}
			// TODO: 사용자 상태 정보가 없을 경우, 실제 상태 처리하는 로직 구현
			return MemberPresenceResponseDto.builder()
					.memberId(memberId)
					.customStatus(memberGlobalStatus.getCustomStatus())
					.globalStatus(PresenceType.OFFLINE)
					.actualStatus(PresenceType.OFFLINE)
					.build();
		}
		return MemberPresenceResponseDto.builder()
				.memberId(memberId)
				.globalStatus(memberPresenceStatus.getGlobalStatus())
				.actualStatus(memberPresenceStatus.getActualStatus())
				.customStatus(memberPresenceStatus.getCustomStatus())
				.build();
	}

	// 서버 멤버 상태 확인
	// GET /state/server/{serverId}/members
	public ServerMemberPresenceResponseDto checkServerMembersState(Long serverId) {
		// 캐싱된 서버 멤버 상태 정보가 없으면 서버 멤버 상태 정보를 조회하여 저장
		if (!serverRedisRepositoryImpl.existsServerMemberPresenceStatus(serverId)) {
			makeServerMemberPresenceStatus(serverId);
		}

		List<ServerMemberStatus> allServerMemberStatus = serverRedisRepositoryImpl.getAllServerMemberStatus(serverId);
		List<MemberPresenceResponseDto> memberStatusResponseDtoList = allServerMemberStatus.stream()
				.map(serverMemberStatus -> MemberPresenceResponseDto.builder()
						.memberId(serverMemberStatus.getMemberId())
						.globalStatus(serverMemberStatus.getGlobalStatus())
						.actualStatus(serverMemberStatus.getActualStatus())
						.customStatus(serverMemberStatus.getCustomStatus())
						.build())
				.toList();

		return ServerMemberPresenceResponseDto.builder()
				.serverId(serverId)
				.memberPresenceStatusList(memberStatusResponseDtoList)
				.build();
	}

	// 서버 멤버 상태 정보를 조회하여 캐싱
	public void makeServerMemberPresenceStatus(Long serverId) {
		Set<Long> serverMemberList = serverRedisRepositoryImpl.getServerMemberList(serverId);
		if (serverMemberList.isEmpty()) {
			ServerMemberListResponseDto responseDto = serviceClient.getServerMemberList(serverId);
			if (responseDto.getMemberIdList().isEmpty()) {
				log.error("[State] StateService: 서버 멤버 정보 없음. serverId: {}", serverId);
				return;
			}
			serverMemberList.addAll(responseDto.getMemberIdList());
		}

		for (Long memberId : serverMemberList) {
			MemberPresenceResponseDto memberStatusResponseDto = checkMemberState(memberId);
			ServerMemberStatus status = ServerMemberStatus.builder()
					.memberId(memberId)
					.globalStatus(memberStatusResponseDto.getGlobalStatus())
					.actualStatus(memberStatusResponseDto.getActualStatus())
					.customStatus(memberStatusResponseDto.getCustomStatus())
					.build();
			serverRedisRepositoryImpl.saveServerMemberPresenceStatus(serverId, memberId, status);
		}
	}

	// 개별 유저의 상태를 업데이트
	public void updateMemberPresenceStatus(Long memberId, PresenceType globalStatus) {
		MemberPresenceStatus memberPresenceStatus = memberRedisRepositoryImpl.getMemberPresenceStatus(memberId);
		if (memberPresenceStatus == null) {
			throw new ErrorHandler(ErrorStatus.MEMBER_PRESENCE_STATE_NOT_FOUND);
		}
		memberPresenceStatus.updateCustomStatus(globalStatus);
		memberRedisRepositoryImpl.saveMemberPresenceStatus(memberId, memberPresenceStatus);

		PresenceEventDto presenceEventDto = PresenceEventDto.builder()
				.memberId(memberId)
				.globalStatus(globalStatus)
				.actualStatus(memberPresenceStatus.getActualStatus())
				.customStatus(memberPresenceStatus.getCustomStatus())
				.lastActivityTime(memberPresenceStatus.getLastActivityTime())
				.build();
		kafkaProducerService.sendPresenceEvent(presenceEventDto);
	}
}
