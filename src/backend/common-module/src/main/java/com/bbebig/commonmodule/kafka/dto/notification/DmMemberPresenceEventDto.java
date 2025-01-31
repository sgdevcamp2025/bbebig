package com.bbebig.commonmodule.kafka.dto.notification;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper=false)
public class DmMemberPresenceEventDto extends NotificationEventDto {

	private Long targetMemberId;

	private Long channelId;

	private String globalStatus; // ONLINE, OFFLINE, AWAY, BUSY, INVISIBLE
}
