package com.bbebig.commonmodule.kafka.dto.notification;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Data
@EqualsAndHashCode(callSuper=false)
public class DmActionEventDto extends NotificationEventDto {

	private Long channelId;

	private Long channelName;

	private String status; // CREATE, UPDATE, DELETE
}
