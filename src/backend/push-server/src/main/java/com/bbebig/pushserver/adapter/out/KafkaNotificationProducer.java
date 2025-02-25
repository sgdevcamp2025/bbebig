package com.bbebig.pushserver.adapter.out;

import com.bbebig.commonmodule.kafka.dto.notification.NotificationEventDto;
import com.bbebig.pushserver.application.port.out.SendNotificationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaNotificationProducer implements SendNotificationPort {

	@Value("${spring.kafka.topic.notification-event}")
	private String notificationEventTopic;

	private final KafkaTemplate<String, NotificationEventDto> kafkaTemplateForNotificationEvent;

	@Override
	public void sendNotification(NotificationEventDto eventDto) {
		kafkaTemplateForNotificationEvent.send(notificationEventTopic, eventDto);

	}
}
