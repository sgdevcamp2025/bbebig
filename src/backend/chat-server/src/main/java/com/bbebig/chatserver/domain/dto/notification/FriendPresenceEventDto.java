package com.bbebig.chatserver.domain.dto.notification;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FriendPresenceEventDto extends NotificationEventDto {

	private Long friendId;

	private String globalStatus; // ONLINE, OFFLINE, AWAY, BUSY, INVISIBLE

}
