package com.bbebig.stateserver.service;

import com.bbebig.commonmodule.kafka.dto.serverEvent.*;
import com.bbebig.commonmodule.redis.domain.ServerMemberStatus;
import com.bbebig.stateserver.dto.StateResponseDto.MemberStatusResponseDto;
import com.bbebig.stateserver.repository.MemberRedisRepositoryImpl;
import com.bbebig.stateserver.repository.ServerRedisRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServerEventConsumerService {

	private final StateService stateService;
	private final MemberRedisRepositoryImpl memberRedisRepositoryImpl;
	private final ServerRedisRepositoryImpl serverRedisRepositoryImpl;

	@KafkaListener(topics = "${spring.kafka.topic.server-event}", groupId = "${spring.kafka.consumer.group-id.server-event}", containerFactory = "serverEventListener")
	public void consumeForServerEvent(ServerEventDto serverEventDto) {
		if (serverEventDto == null) {
			log.error("[State] ServerEventConsumerService: 서버 이벤트 정보 없음");
			return;
		}

		if (serverEventDto.getType().equals(ServerEventType.SERVER_MEMBER_PRESENCE.toString())) {
			ServerMemberPresenceEventDto eventDto = (ServerMemberPresenceEventDto) serverEventDto;

			Long serverId = eventDto.getServerId();

			// 캐싱된 서버 멤버 상태 정보가 없으면 서버 멤버 상태 정보를 조회하여 저장
			if (!serverRedisRepositoryImpl.existsServerMemberPresenceStatus(serverId)) {
				stateService.makeServerMemberPresenceStatus(serverId);
			}

			// 캐싱된 멤버별로 참여한 서버 목록이 없으면 멤버별로 참여한 서버 목록을 조회하여 저장
			if (!memberRedisRepositoryImpl.existsMemberServerList(eventDto.getMemberId())) {
				stateService.makeMemberServerList(eventDto.getMemberId());
			}

			ServerMemberStatus status = ServerMemberStatus.builder()
					.memberId(eventDto.getMemberId())
					.globalStatus(eventDto.getGlobalStatus())
					.actualStatus(eventDto.getActualStatus())
					.build();

			serverRedisRepositoryImpl.saveServerMemberPresenceStatus(serverId, eventDto.getMemberId(), status);

		} else if (serverEventDto.getType().equals(ServerEventType.SERVER_MEMBER_ACTION.toString())) {
			ServerMemberActionEventDto eventDto = (ServerMemberActionEventDto) serverEventDto;

			Long serverId = eventDto.getServerId();

			// 캐싱된 서버 멤버 상태 정보가 없으면 서버 멤버 상태 정보를 조회하여 저장
			if (!serverRedisRepositoryImpl.existsServerMemberPresenceStatus(serverId)) {
				stateService.makeServerMemberPresenceStatus(serverId);
			}
			handleMemberActionEvent(eventDto);

		} else if (serverEventDto.getType().equals(ServerEventType.SERVER_ACTION.toString())) {
			ServerActionEventDto eventDto = (ServerActionEventDto) serverEventDto;
			handleServerActionEvent(eventDto);
		} else if (serverEventDto.getType().equals(ServerEventType.SERVER_CHANNEL.toString())) {
			ServerChannelEventDto eventDto = (ServerChannelEventDto) serverEventDto;
			handleServerChannelEvent(eventDto);
		}
		else {
			if (!serverEventDto.getType().equals(ServerEventType.SERVER_CATEGORY.toString())) {
				log.error("[State] ServerEventConsumerService: 서버 이벤트 타입이 잘못되었습니다. serverEventDto: {}", serverEventDto);
			}
		}

	}

	// 서버 멤버 행동 이벤트 처리
	private void handleMemberActionEvent(ServerMemberActionEventDto eventDto) {
		Long serverId = eventDto.getServerId();

		if (eventDto.getType().equals("JOIN")) {
			serverRedisRepositoryImpl.addServerMemberToSet(serverId, eventDto.getMemberId());
			memberRedisRepositoryImpl.addMemberServerToSet(eventDto.getMemberId(), serverId);

			// 멤버 상태 정보를 조회하여 서버별 멤버 상태 정보에 저장
			MemberStatusResponseDto memberStatusResponseDto = stateService.checkMemberState(eventDto.getMemberId());
			if (memberStatusResponseDto == null) {
				log.error("[State] ServerEventConsumerService: 서버 멤버 상태 정보 조회 실패. memberId: {}", eventDto.getMemberId());
				return;
			}

			ServerMemberStatus status = ServerMemberStatus.builder()
					.memberId(eventDto.getMemberId())
					.globalStatus(memberStatusResponseDto.getGlobalStatus())
					.actualStatus(memberStatusResponseDto.getActualStatus())
					.build();
			serverRedisRepositoryImpl.saveServerMemberPresenceStatus(serverId, eventDto.getMemberId(), status);

		} else if (eventDto.getType().equals("LEAVE")) {
			serverRedisRepositoryImpl.removeServerMemberFromSet(serverId, eventDto.getMemberId());
			serverRedisRepositoryImpl.removeServerMemberPresenceStatus(serverId, eventDto.getMemberId());
			memberRedisRepositoryImpl.removeMemberServerFromSet(eventDto.getMemberId(), serverId);
		} else {
			log.error("[State] ServerEventConsumerService: 서버 멤버 행동 이벤트 타입이 잘못되었습니다. memberId : {}, eventDto: {}", eventDto.getMemberId(), eventDto);
		}
	}

	public void handleServerActionEvent(ServerActionEventDto eventDto) {
		Long serverId = eventDto.getServerId();

		if (eventDto.getStatus().equals("CREATE")) {
			stateService.makeServerMemberPresenceStatus(serverId);
			stateService.makeServerMemberList(serverId);
			stateService.makeServerChannelList(serverId);
		} else if (eventDto.getStatus().equals("DELETE")) {
			serverRedisRepositoryImpl.deleteServerMemberStatus(serverId);
			serverRedisRepositoryImpl.deleteServerMemberList(serverId);
			serverRedisRepositoryImpl.deleteServerChannelList(serverId);
		} else {
			if (!eventDto.getStatus().equals("UPDATE")) {
				log.error("[State] ServerEventConsumerService: 서버 액션 이벤트 타입이 잘못되었습니다. eventDto: {}", eventDto);
			}
		}

	}

	private void handleServerChannelEvent(ServerChannelEventDto eventDto) {
		Long serverId = eventDto.getServerId();

		if (eventDto.getType().equals("CREATE")) {
			if (!serverRedisRepositoryImpl.existsServerChannelList(serverId)) {
				stateService.makeServerChannelList(serverId);
			}
			serverRedisRepositoryImpl.addServerChannelToSet(serverId, eventDto.getChannelId());
		} else if (eventDto.getType().equals("DELETE")) {
			// 서버에 참여중인 모든 유저에 대해 최근 채널 캐싱 정보를 삭제
			serverRedisRepositoryImpl.getServerMemberList(serverId).forEach(memberId -> {
				memberRedisRepositoryImpl.deleteMemberRecentServerChannel(memberId, eventDto.getChannelId());
			});
			// TODO : 멤버별 안읽은 메시지 카운트 캐싱에서 해당 채널을 지우는 로직 추가
			serverRedisRepositoryImpl.removeServerChannelFromSet(serverId, eventDto.getChannelId());
		} else {
			if (!eventDto.getType().equals("UPDATE")) {
				log.error("[State] ServerEventConsumerService: 서버 채널 이벤트 타입이 잘못되었습니다. eventDto: {}", eventDto);
			}
		}
		serverRedisRepositoryImpl.saveServerChannelSet(serverId, eventDto.getChannelIdList());
	}

}
