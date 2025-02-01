package com.bbebig.chatserver.domain.kafka.dto.notification;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DmMemberPresenceEventDto extends NotificationEventDto {

	private Long targetMemberId;

	private Long channelId;

	private String globalStatus; // ONLINE, OFFLINE, AWAY, BUSY, INVISIBLE
}
