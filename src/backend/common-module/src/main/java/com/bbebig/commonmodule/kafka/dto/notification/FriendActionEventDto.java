package com.bbebig.commonmodule.kafka.dto.notification;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper=false)
public class FriendActionEventDto extends NotificationEventDto {

	private Long friendMemberId;

	private String status; // ADD, DELETE, UPDATE

	private String friendNickName;

	private String friendProfileImageUrl;

	@JsonCreator
	public FriendActionEventDto(
			@JsonProperty("notificationId") Long notificationId,
			@JsonProperty("notificationEventType") NotificationEventType notificationEventType,
			@JsonProperty("friendMemberId") Long friendMemberId,
			@JsonProperty("status") String status,
			@JsonProperty("friendNickName") String friendNickName,
			@JsonProperty("friendProfileImageUrl") String friendProfileImageUrl
	) {
		super(notificationId, notificationEventType);
		this.friendMemberId = friendMemberId;
		this.status = status;
		this.friendNickName = friendNickName;
		this.friendProfileImageUrl = friendProfileImageUrl;
	}
}
