package com.bbebig.commonmodule.kafka.dto.notification;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper=false)
public class ServerUnreadEventDto extends NotificationEventDto{

	private Long serverId;

	private Long channelId;

	private String status; // UNREAD
}
