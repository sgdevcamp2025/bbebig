package com.bbebig.stateserver.service;

import com.bbebig.commonmodule.clientDto.StateFeignResponseDto;
import com.bbebig.commonmodule.clientDto.StateFeignResponseDto.MemberPresenceResponseDto;
import com.bbebig.commonmodule.kafka.dto.model.PresenceType;
import com.bbebig.commonmodule.kafka.dto.serverEvent.*;
import com.bbebig.commonmodule.kafka.dto.serverEvent.status.ServerActionStatus;
import com.bbebig.commonmodule.kafka.dto.serverEvent.status.ServerChannelStatus;
import com.bbebig.commonmodule.kafka.dto.serverEvent.status.ServerMemberActionStatus;
import com.bbebig.commonmodule.redis.domain.ServerMemberStatus;
import com.bbebig.stateserver.repository.MemberRedisRepositoryImpl;
import com.bbebig.stateserver.repository.ServerRedisRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

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

		// TODO: 서버멤버 존재 이벤트 처리 방식 고민. 멤버 온라인 이벤트 발행 시점에 같이 처리할지 고민.
		if (serverEventDto.getType().equals(ServerEventType.SERVER_MEMBER_PRESENCE)) {
			ServerMemberPresenceEventDto eventDto = (ServerMemberPresenceEventDto) serverEventDto;

			Long serverId = eventDto.getServerId();

			ServerMemberStatus status = ServerMemberStatus.builder()
					.memberId(eventDto.getMemberId())
					.globalStatus(eventDto.getGlobalStatus())
					.actualStatus(eventDto.getActualStatus())
					.customStatus(eventDto.getCustomStatus())
					.build();

			if (!serverRedisRepositoryImpl.existsServerMemberPresenceStatus(serverId)) {
				stateService.makeServerMemberPresenceStatus(serverId);
			} else {
				serverRedisRepositoryImpl.saveServerMemberPresenceStatus(serverId, eventDto.getMemberId(), status);
			}

		} else if (serverEventDto.getType().equals(ServerEventType.SERVER_MEMBER_ACTION)) {
			ServerMemberActionEventDto eventDto = (ServerMemberActionEventDto) serverEventDto;
			handleMemberActionEvent(eventDto);

		} else if (serverEventDto.getType().equals(ServerEventType.SERVER_ACTION)) {
			ServerActionEventDto eventDto = (ServerActionEventDto) serverEventDto;
			handleServerActionEvent(eventDto);
		} else if (serverEventDto.getType().equals(ServerEventType.SERVER_CHANNEL)) {
			ServerChannelEventDto eventDto = (ServerChannelEventDto) serverEventDto;
			handleServerChannelEvent(eventDto);
		}
		else {
			if (!serverEventDto.getType().equals(ServerEventType.SERVER_CATEGORY)) {
				log.error("[State] ServerEventConsumerService: 서버 이벤트 타입이 잘못되었습니다. serverEventDto: {}", serverEventDto);
			}
			return;
		}

	}

	// 서버 멤버 행동 이벤트 처리
	private void handleMemberActionEvent(ServerMemberActionEventDto eventDto) {
		Long serverId = eventDto.getServerId();

		if (eventDto.getStatus().equals(ServerMemberActionStatus.JOIN)) {
			// 멤버 상태 정보를 조회하여 서버별 멤버 상태 정보에 저장
			MemberPresenceResponseDto memberStatusResponseDto = stateService.checkMemberState(eventDto.getMemberId());
			if (memberStatusResponseDto == null) {
				log.error("[State] ServerEventConsumerService: 서버 멤버 상태 정보 조회 실패. memberId: {}", eventDto.getMemberId());
				return;
			}

			ServerMemberStatus status = ServerMemberStatus.builder()
					.memberId(eventDto.getMemberId())
					.globalStatus(memberStatusResponseDto.getGlobalStatus())
					.actualStatus(memberStatusResponseDto.getActualStatus())
					.customStatus(memberStatusResponseDto.getCustomStatus())
					.build();
			serverRedisRepositoryImpl.saveServerMemberPresenceStatus(serverId, eventDto.getMemberId(), status);

		} else if (eventDto.getStatus().equals(ServerMemberActionStatus.LEAVE)) {
			serverRedisRepositoryImpl.removeServerMemberPresenceStatus(serverId, eventDto.getMemberId());
		} else {
			log.error("[State] ServerEventConsumerService: 서버 멤버 행동 이벤트 타입이 잘못되었습니다. memberId : {}, eventDto: {}", eventDto.getMemberId(), eventDto);
		}
	}

	public void handleServerActionEvent(ServerActionEventDto eventDto) {
		Long serverId = eventDto.getServerId();

		if (eventDto.getStatus().equals(ServerActionStatus.CREATE)) {
			stateService.makeServerMemberPresenceStatus(serverId);
		} else if (eventDto.getStatus().equals(ServerActionStatus.UPDATE)) {
			serverRedisRepositoryImpl.deleteServerMemberStatus(serverId);
		} else {
			if (!eventDto.getStatus().equals(ServerActionStatus.DELETE)) {
				log.error("[State] ServerEventConsumerService: 서버 액션 이벤트 타입이 잘못되었습니다. eventDto: {}", eventDto);
			}
		}

	}

	private void handleServerChannelEvent(ServerChannelEventDto eventDto) {
		Long serverId = eventDto.getServerId();

		if (eventDto.getStatus().equals(ServerChannelStatus.DELETE)) {
			serverRedisRepositoryImpl.removeServerChannelFromSet(serverId, eventDto.getChannelId());
		} else {
			if (!(eventDto.getStatus().equals(ServerChannelStatus.UPDATE) || eventDto.getStatus().equals(ServerChannelStatus.CREATE))) {
				log.error("[State] ServerEventConsumerService: 서버 채널 이벤트 타입이 잘못되었습니다. eventDto: {}", eventDto);
			}
		}
	}

}
