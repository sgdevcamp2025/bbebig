package com.bbebig.serviceserver.global.kafka;

import com.bbebig.commonmodule.kafka.dto.PresenceEventDto;
import com.bbebig.commonmodule.kafka.dto.notification.NotificationEventDto;
import com.bbebig.commonmodule.kafka.dto.serverEvent.ServerEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

	@Value("${spring.kafka.topic.server-event}")
	private String serverEventTopic;

	@Value("${spring.kafka.topic.notification-event}")
	private String notificationEventTopic;

	private final KafkaTemplate<String, ServerEventDto> kafkaTemplateForServerEvent;

	private final KafkaTemplate<String, NotificationEventDto> kafkaTemplateForNotificationEvent;

	// 서버 이벤트 메시지 전송
	public void sendServerEvent(ServerEventDto serverEventDto) {
		kafkaTemplateForServerEvent.send(serverEventTopic, serverEventDto);
	}

	// 알림 이벤트 메시지 전송
	public void sendNotificationEvent(NotificationEventDto notificationEventDto) {
		kafkaTemplateForNotificationEvent.send(notificationEventTopic, notificationEventDto);
	}
}
