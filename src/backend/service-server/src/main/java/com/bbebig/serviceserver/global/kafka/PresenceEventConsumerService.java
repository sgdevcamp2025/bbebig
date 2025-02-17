package com.bbebig.serviceserver.global.kafka;

import com.bbebig.commonmodule.kafka.dto.PresenceEventDto;
import com.bbebig.commonmodule.kafka.dto.serverEvent.ServerEventType;
import com.bbebig.commonmodule.kafka.dto.serverEvent.ServerMemberPresenceEventDto;
import com.bbebig.serviceserver.server.service.ServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import static com.bbebig.commonmodule.clientDto.serviceServer.CommonServiceServerClientResponseDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PresenceEventConsumerService {

	private final ServerService serverService;
	private final KafkaProducerService kafkaProducerService;

	@KafkaListener(topics = "${spring.kafka.topic.presence-event}", groupId = "${spring.kafka.consumer.group-id.presence-event}", containerFactory = "presenceEventListener")
	public void consumeForPresenceEvent(PresenceEventDto presenceEventDto) {
		if (presenceEventDto == null) {
			log.error("[State] PresenceEventConsumerService: 프레즌스 이벤트 정보 없음");
			return;
		}

		handleEvent(presenceEventDto);
	}

	private void handleEvent(PresenceEventDto presenceEventDto) {
		// 개발용 로그
		log.info("[State] PresenceEventConsumerService: 온라인 이벤트 처리. memberId: {}, globalStatus: {}, actualStatus: {}, lastActivityTime: {}", presenceEventDto.getMemberId(), presenceEventDto.getGlobalStatus(), presenceEventDto.getActualStatus(), presenceEventDto.getLastActivityTime());
		Long memberId = presenceEventDto.getMemberId();
		MemberServerListResponseDto memberServerList = serverService.getMemberServerList(memberId);
		if (memberServerList == null) {
			log.error("[State] PresenceEventConsumerService: 서버 정보 없음. memberId: {}", memberId);
			return;
		}
		memberServerList.getServerIdList().forEach(serverId -> {
			ServerMemberPresenceEventDto serverMemberPresenceEventDto = ServerMemberPresenceEventDto.builder()
					.serverId(serverId)
					.type(ServerEventType.SERVER_MEMBER_PRESENCE)
					.memberId(memberId)
					.globalStatus(presenceEventDto.getGlobalStatus())
					.actualStatus(presenceEventDto.getActualStatus())
					.build();
			kafkaProducerService.sendServerEvent(serverMemberPresenceEventDto);
		});

	}
}
