package com.bbebig.stateserver.service;

import com.bbebig.commonmodule.kafka.dto.PresenceEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

	@Value("${spring.kafka.topic.presence-event}")
	private String channelEventTopic;

	private final KafkaTemplate<String, PresenceEventDto> kafkaTemplateForPresenceEvent;

	// Presence 이벤트 메시지 전송
	public void sendPresenceEvent(PresenceEventDto presenceEventDto) {
		kafkaTemplateForPresenceEvent.send(channelEventTopic, presenceEventDto);
	}
}
