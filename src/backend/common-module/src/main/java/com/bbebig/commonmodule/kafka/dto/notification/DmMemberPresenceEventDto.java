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
public class DmMemberPresenceEventDto extends NotificationEventDto {

	private Long targetMemberId;

	private Long channelId;

	private PresenceType globalStatus; // ONLINE, OFFLINE, AWAY, BUSY, INVISIBLE

	@JsonCreator
	public DmMemberPresenceEventDto(
			@JsonProperty("notificationId") Long notificationId,
			@JsonProperty("notificationEventType") NotificationEventType notificationEventType,
			@JsonProperty("targetMemberId") Long targetMemberId,
			@JsonProperty("channelId") Long channelId,
			@JsonProperty("globalStatus") PresenceType globalStatus
	) {
		super(notificationId, notificationEventType);
		this.targetMemberId = targetMemberId;
		this.channelId = channelId;
		this.globalStatus = globalStatus;
	}
}
