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

	private Long friendMemberId;

	private PresenceType globalStatus;

	@JsonCreator
	public FriendPresenceEventDto(
			@JsonProperty("memberId") Long memberId,
			@JsonProperty("type") NotificationEventType type,
			@JsonProperty("friendMemberId") Long friendMemberId,
			@JsonProperty("globalStatus") PresenceType globalStatus
	) {
		super(memberId, type);
		this.friendMemberId = friendMemberId;
		this.globalStatus = globalStatus;
		this.actualStatus = actualStatus;
	}


}
