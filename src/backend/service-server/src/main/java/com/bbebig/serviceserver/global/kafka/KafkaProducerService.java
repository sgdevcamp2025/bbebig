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

	@Value("${spring.data.kafka.topic.server-event}")
	private String serverEventTopic;

	@Value("${spring.data.kafka.topic.notification-event}")
	private String notificationEventTopic;

	private final KafkaTemplate<String, ServerEventDto> kafkaTemplateForServer;

	private final KafkaTemplate<String, NotificationEventDto> kafkaTemplateForNotification;

	// 서버 이벤트 메시지 전송
	public void sendServerEvent(ServerEventDto serverEventDto) {
		kafkaTemplateForServer.send(serverEventTopic, serverEventDto);
	}

	// 알림 이벤트 메시지 전송
	public void sendNotificationEvent(NotificationEventDto notificationEventDto) {
		kafkaTemplateForNotification.send(notificationEventTopic, notificationEventDto);
	}
}
