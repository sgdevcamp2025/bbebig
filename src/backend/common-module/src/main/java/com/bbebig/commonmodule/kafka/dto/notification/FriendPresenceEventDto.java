package com.bbebig.commonmodule.kafka.dto.notification;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper=false)
public class FriendPresenceEventDto extends NotificationEventDto {

	private Long friendId;

	private String globalStatus; // ONLINE, OFFLINE, AWAY, BUSY, INVISIBLE



}
