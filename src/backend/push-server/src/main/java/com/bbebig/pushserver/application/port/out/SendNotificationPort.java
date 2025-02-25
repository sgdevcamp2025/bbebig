package com.bbebig.pushserver.application.port.out;

import com.bbebig.commonmodule.kafka.dto.notification.NotificationEventDto;

public interface SendNotificationPort {

	void sendNotification(NotificationEventDto messageDto);
}
