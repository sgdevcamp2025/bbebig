package com.bbebig.stateserver.service;

import com.bbebig.commonmodule.kafka.dto.serverEvent.ServerEventDto;
import com.bbebig.commonmodule.kafka.dto.serverEvent.ServerEventType;
import com.bbebig.commonmodule.kafka.dto.serverEvent.ServerMemberActionEventDto;
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

import java.util.Set;

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
			ServerMemberActionEventDto eventDto = (ServerMemberActionEventDto) serverEventDto;

			Long serverId = eventDto.getServerId();

			// 캐싱된 서버 멤버 상태 정보가 없으면 서버 멤버 상태 정보를 조회하여 저장
			if (!redisRepository.existsServerMemberPresenceStatus(serverId)) {
				makeServerMemberPresenceStatus(serverId);
			}
			handleMemberActionEvent(eventDto);

		} else {
			if (!(serverEventDto.getType().equals(ServerEventType.SERVER_CATEGORY.toString()) ||
					serverEventDto.getType().equals(ServerEventType.SERVER_CHANNEL.toString()))) {
				log.error("[State] ServerEventConsumerService: 서버 이벤트 타입이 잘못되었습니다. serverEventDto: {}", serverEventDto);
			}
		}

	}

	private void handleMemberActionEvent(ServerMemberActionEventDto eventDto) {
		Long serverId = eventDto.getServerId();

		if (eventDto.getType().equals("JOIN")) {
			redisRepository.addServerMemberToSet(serverId, eventDto.getMemberId());

			// 서버 멤버 상태 정보를 조회하여 저장
			MemberStatusResponseDto memberStatusResponseDto = stateService.checkMemberState(eventDto.getMemberId());
			if (memberStatusResponseDto == null) {
				log.error("[State] ServerEventConsumerService: 서버 멤버 상태 정보 조회 실패. memberId: {}", eventDto.getMemberId());
				return;
			}

			ServerMemberStatus status = ServerMemberStatus.builder()
					.globalStatus(memberStatusResponseDto.getGlobalStatus())
					.actualStatus(memberStatusResponseDto.getActualStatus())
					.build();
			redisRepository.saveServerMemberPresenceStatus(serverId, eventDto.getMemberId(), status);

		} else if (eventDto.getType().equals("LEAVE")) {
			redisRepository.removeServerMemberFromSet(serverId, eventDto.getMemberId());
			redisRepository.removeServerMemberPresenceStatus(serverId, eventDto.getMemberId());
		} else {
			log.error("[State] ServerEventConsumerService: 서버 멤버 행동 이벤트 타입이 잘못되었습니다. memberId : {}, eventDto: {}", eventDto.getMemberId(), eventDto);
		}
	}

	// 서버 멤버 상태 정보를 조회하여 저장
	private void makeServerMemberPresenceStatus(Long serverId) {

		// 서버 멤버 목록이 없으면 서버 멤버 목록을 조회하여 저장
		if (!redisRepository.existsServerMemberList(serverId)) {
			makeServerMemberList(serverId);
		}

		Set<Long> serverMemberList = redisRepository.getServerMemberList(serverId);
		if (serverMemberList == null || serverMemberList.isEmpty()) {
			log.error("[State] ServerEventConsumerService: 서버 멤버 정보 생성 실패. serverId: {}", serverId);
			return;
		}

		for (Long memberId : serverMemberList) {
			MemberStatusResponseDto memberStatusResponseDto = stateService.checkMemberState(memberId);
			ServerMemberStatus status = ServerMemberStatus.builder()
					.globalStatus(memberStatusResponseDto.getGlobalStatus())
					.actualStatus(memberStatusResponseDto.getActualStatus())
					.build();
			redisRepository.saveServerMemberPresenceStatus(serverId, memberId, status);
		}
	}

	// 서버 멤버 목록을 조회하여 저장
	private void makeServerMemberList(Long serverId) {
		ServerMemberListResponseDto responseDto = serviceClient.getServerMemberList(serverId);
		if (responseDto == null) {
			log.error("[State] ServerEventConsumerService: 서버 멤버 정보 불러오기 실패. serverId: {}", serverId);
			return;
		}
		redisRepository.saveServerMemberSet(serverId, responseDto.getMemberIdList());
	}
}
