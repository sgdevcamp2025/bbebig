package com.bbebig.userserver.member.service;

import com.bbebig.commonmodule.kafka.dto.MemberEventDto;
import com.bbebig.commonmodule.kafka.dto.notification.NotificationEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaProducerService {

	@Value("${spring.kafka.topic.member-event}")
	private String memberEventTopic;

	@Value("${spring.kafka.topic.notification-event}")
	private String notificationEventTopic;

	private final KafkaTemplate<String, MemberEventDto> kafkaTemplateForMemberEvent;

	private final KafkaTemplate<String, NotificationEventDto> kafkaTemplateForNotificationEvent;

	public void sendMemberEvent(MemberEventDto memberEventDto) {
		kafkaTemplateForMemberEvent.send(memberEventTopic, memberEventDto);
	}

	public void sendNotificationEvent(NotificationEventDto notificationEventDto) {
		kafkaTemplateForNotificationEvent.send(notificationEventTopic, notificationEventDto);
	}
}
