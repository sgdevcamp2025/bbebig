package com.bbebig.stateserver.service;

import com.bbebig.commonmodule.redis.domain.MemberPresenceStatus;
import com.bbebig.commonmodule.redis.domain.ServerMemberStatus;
import com.bbebig.stateserver.client.ServiceClient;
import com.bbebig.stateserver.dto.ServiceResponseDto.*;
import com.bbebig.stateserver.dto.StateResponseDto.*;
import com.bbebig.stateserver.repository.DmRedisRepositoryImpl;
import com.bbebig.stateserver.repository.MemberRedisRepositoryImpl;
import com.bbebig.stateserver.repository.ServerRedisRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class StateService {

	private final MemberRedisRepositoryImpl memberRedisRepositoryImpl;
	private final ServerRedisRepositoryImpl serverRedisRepositoryImpl;
	private final DmRedisRepositoryImpl dmRedisRepositoryImpl;

	private final ServiceClient serviceClient;

	// 사용자 상태 확인
	// GET /state/member/{memberId}
	public MemberStatusResponseDto checkMemberState(Long memberId) {
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

	// 서버 멤버 상태 확인
	// GET /state/server/{serverId}/members
	public ServerMemberPresenceResponseDto checkServerMembersState(Long serverId) {
		// 캐싱된 서버 멤버 상태 정보가 없으면 서버 멤버 상태 정보를 조회하여 저장
		if (!serverRedisRepositoryImpl.existsServerMemberPresenceStatus(serverId)) {
			makeServerMemberPresenceStatus(serverId);
		}

		List<ServerMemberStatus> allServerMemberStatus = serverRedisRepositoryImpl.getAllServerMemberStatus(serverId);
		List<MemberPresenceStatusDto> memberStatusResponseDtoList = allServerMemberStatus.stream()
				.map(serverMemberStatus -> MemberPresenceStatusDto.builder()
						.memberId(serverMemberStatus.getMemberId())
						.globalStatus(serverMemberStatus.getGlobalStatus())
						.actualStatus(serverMemberStatus.getActualStatus())
						.build())
				.toList();

		return ServerMemberPresenceResponseDto.builder()
				.serverId(serverId)
				.memberPresenceStatusList(memberStatusResponseDtoList)
				.build();
	}

	// 서버 멤버 상태 정보를 조회하여 캐싱
	public void makeServerMemberPresenceStatus(Long serverId) {

		// 캐싱된 서버에 참여한 멤버 목록이 없으면 서버에 참여한 멤버 목록을 조회하여 저장
		if (!serverRedisRepositoryImpl.existsServerMemberList(serverId)) {
			makeServerMemberList(serverId);
		}

		Set<Long> serverMemberList = serverRedisRepositoryImpl.getServerMemberList(serverId);
		if (serverMemberList == null || serverMemberList.isEmpty()) {
			log.error("[State] ServerEventConsumerService: 서버 멤버 정보 생성 실패. serverId: {}", serverId);
			return;
		}

		for (Long memberId : serverMemberList) {
			MemberStatusResponseDto memberStatusResponseDto = checkMemberState(memberId);
			ServerMemberStatus status = ServerMemberStatus.builder()
					.memberId(memberId)
					.globalStatus(memberStatusResponseDto.getGlobalStatus())
					.actualStatus(memberStatusResponseDto.getActualStatus())
					.build();
			serverRedisRepositoryImpl.saveServerMemberPresenceStatus(serverId, memberId, status);
		}
	}

	// 서버에 참여한 멤버 목록을 조회하여 캐싱
	public void makeServerMemberList(Long serverId) {
		ServerMemberListResponseDto responseDto = serviceClient.getServerMemberList(serverId);
		if (responseDto == null) {
			log.error("[State] ServerEventConsumerService: 서버 멤버 정보 불러오기 실패. serverId: {}", serverId);
			return;
		}
		serverRedisRepositoryImpl.saveServerMemberSet(serverId, responseDto.getMemberIdList());
	}

	// 멤버별로 참여한 서버 목록을 조회하여 캐싱
	public void makeMemberServerList(Long memberId) {
		MemberServerListResponseDto memberServerList = serviceClient.getMemberServerList(memberId);
		if (memberServerList == null) {
			log.error("[State] ServerEventConsumerService: 서버 멤버 정보 불러오기 실패. memberId: {}", memberId);
			return;
		}
		memberRedisRepositoryImpl.saveMemberServerSet(memberId, memberServerList.getServerIdList());
	}

	// 디엠 채널별로 참여한 유저 조회 후 캐싱
	public void makeDmMemberList(Long channelId) {
		DmMemberListResponseDto dmMemberList = serviceClient.getDmMemberList(channelId);
		if (dmMemberList == null) {
			log.error("[State] ServerEventConsumerService: 디엠 멤버 정보 불러오기 실패. channelId: {}", channelId);
			return;
		}
		dmRedisRepositoryImpl.saveDmMemberSet(channelId, dmMemberList.getDmMemberIdList());
	}

}
