package com.bbebig.stateserver.service;

import com.bbebig.commonmodule.kafka.dto.serverEvent.ServerEventDto;
import com.bbebig.commonmodule.kafka.dto.serverEvent.ServerEventType;
import com.bbebig.commonmodule.kafka.dto.serverEvent.ServerMemberPresenceEventDto;
import com.bbebig.stateserver.client.ServiceClient;
import com.bbebig.stateserver.domain.ServerMemberStatus;
import com.bbebig.stateserver.dto.ServiceResponseDto.ServerMemberListResponseDto;
import com.bbebig.stateserver.dto.StateResponseDto.MemberStatusResponseDto;
import com.bbebig.stateserver.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServerEventConsumerService {

	private final StateService stateService;
	private final RedisRepository redisRepository;
	private final ServiceClient serviceClient;

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
			if (!redisRepository.existsServerMemberPresenceStatus(serverId)) {
				makeServerMemberPresenceStatus(serverId);
			}

			ServerMemberStatus status = ServerMemberStatus.builder()
					.globalStatus(eventDto.getGlobalStatus())
					.actualStatus(eventDto.getActualStatus())
					.build();

			redisRepository.saveServerMemberPresenceStatus(serverId, eventDto.getMemberId(), status);

		} else if (serverEventDto.getType().equals(ServerEventType.SERVER_MEMBER_ACTION.toString())) {
			// TODO : 추후 서버 멤버 행동 이벤트 처리 로직 추가
		} else {
			if (!(serverEventDto.getType().equals(ServerEventType.SERVER_CATEGORY.toString()) ||
					serverEventDto.getType().equals(ServerEventType.SERVER_CHANNEL.toString()))) {
				log.error("[State] ServerEventConsumerService: 서버 이벤트 타입이 잘못되었습니다. serverEventDto: {}", serverEventDto);
			}
		}

	}

	// 서버 멤버 상태 정보를 조회하여 저장
	private void makeServerMemberPresenceStatus(Long serverId) {
		if (!redisRepository.existsServerMemberList(serverId)) {
			ServerMemberListResponseDto responseDto = serviceClient.getServerMemberList(serverId);
			if (responseDto == null) {
				log.error("[State] ServerEventConsumerService: 서버 멤버 정보 없음. serverId: {}", serverId);
				return;
			}
			redisRepository.saveServerMemberList(serverId, responseDto.getMemberIdList());
		}

		for (Long memberId : redisRepository.loadServerMemberList(serverId)) {
			MemberStatusResponseDto memberStatusResponseDto = stateService.checkMemberState(memberId);
			ServerMemberStatus status = ServerMemberStatus.builder()
					.globalStatus(memberStatusResponseDto.getGlobalStatus())
					.actualStatus(memberStatusResponseDto.getActualStatus())
					.build();
			redisRepository.saveServerMemberPresenceStatus(serverId, memberId, status);
		}
	}
}
