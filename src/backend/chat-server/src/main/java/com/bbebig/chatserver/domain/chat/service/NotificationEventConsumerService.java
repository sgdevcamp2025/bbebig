package com.bbebig.chatserver.domain.chat.service;

import com.bbebig.chatserver.domain.kafka.dto.notification.*;
import com.bbebig.chatserver.domain.chat.repository.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationEventConsumerService {

	private final SimpMessageSendingOperations messagingTemplate;

	private final SessionManager sessionManager;

	@KafkaListener(topics = "${spring.kafka.topic.notification-event}", groupId = "${spring.kafka.consumer.group-id.notification-event}", containerFactory = "notificationEventListener")
	public void consumeForNotificationEvent(NotificationEventDto notificationEventDto) {
		if (notificationEventDto == null) {
			log.error("[Chat] NotificationEventConsumerService: 알림 이벤트 정보 없음");
			return;
		}

		if (notificationEventDto instanceof FriendActionEventDto
				|| notificationEventDto instanceof DmMemberActionEventDto
				|| notificationEventDto instanceof FriendPresenceEventDto
				|| notificationEventDto instanceof DmMemberPresenceEventDto) {
			if (sessionManager.isExistMemberId(notificationEventDto.getMemberId())) {
				messagingTemplate.convertAndSend("/queue/" + notificationEventDto.getMemberId() + "/notification", notificationEventDto);
			}
		} else {
			log.error("[Chat] NotificationEventConsumerService: 알려지지 않은 알림 이벤트 타입 수신. NotificationEventDto: {}", notificationEventDto);
		}


	}
}
