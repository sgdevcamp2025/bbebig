package com.bbebig.commonmodule.kafka.dto.notification;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class NotificationEventDto {

	Long memberId;

	NotificationEventType type; // NotificationEventType Enum 참고
}
