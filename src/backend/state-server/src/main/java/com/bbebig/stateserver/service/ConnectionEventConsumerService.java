package com.bbebig.stateserver.service;

import com.bbebig.commonmodule.kafka.dto.ConnectionEventDto;
import com.bbebig.commonmodule.kafka.dto.PresenceEventDto;
import com.bbebig.commonmodule.kafka.dto.model.PresenceType;
import com.bbebig.stateserver.domain.MemberPresenceStatus;
import com.bbebig.stateserver.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectionEventConsumerService {

	private final RedisRepository redisRepository;
	private final KafkaProducerService kafkaProducerService;

	@KafkaListener(topics = "${spring.kafka.topic.connection-event}", groupId = "${spring.kafka.consumer.group-id.connection-event}", containerFactory = "connectionEventListener")
	public void consumeForChannelChatEvent(ConnectionEventDto connectionEventDto) {
		if (connectionEventDto == null) {
			log.error("[State] ConnectionEventConsumerService: 연결 이벤트 정보 없음");
			return;
		}
		MemberPresenceStatus memberPresenceStatus = redisRepository.saveConnectionEvent(connectionEventDto);

		PresenceEventDto presenceEventDto = PresenceEventDto.builder()
				.memberId(connectionEventDto.getMemberId())
				.globalStatus(memberPresenceStatus.getGlobalStatus())
				.actualStatus(memberPresenceStatus.getActualStatus())
				.lastActivityTime(memberPresenceStatus.getLastActivityTime())
				.build();

		kafkaProducerService.sendPresenceEvent(presenceEventDto);
	}

}
