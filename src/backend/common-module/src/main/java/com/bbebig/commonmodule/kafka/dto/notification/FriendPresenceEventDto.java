package com.bbebig.commonmodule.kafka.dto.notification;

import com.bbebig.commonmodule.kafka.dto.model.PresenceType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper=false)
public class FriendPresenceEventDto extends NotificationEventDto {

	private Long friendId;

	private PresenceType globalStatus; // ONLINE, OFFLINE, AWAY, BUSY, INVISIBLE

	@JsonCreator
	public FriendPresenceEventDto(
			@JsonProperty("notificationId") Long notificationId,
			@JsonProperty("notificationEventType") NotificationEventType notificationEventType,
			@JsonProperty("friendId") Long friendId,
			@JsonProperty("globalStatus") PresenceType globalStatus
	) {
		super(notificationId, notificationEventType);
		this.friendId = friendId;
		this.globalStatus = globalStatus;
	}


}
