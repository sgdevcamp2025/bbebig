package com.bbebig.commonmodule.kafka.dto.notification;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper=false)
public class FriendRequestEventDto extends NotificationEventDto {

	private Long friendMemberId;

	private String friendNickName;

	private String friendProfileImageUrl;

	private String status; // SEND, RECEIVE, ACCEPT, REJECT

	@JsonCreator
	public FriendRequestEventDto(
			@JsonProperty("notificationId") Long notificationId,
			@JsonProperty("notificationEventType") NotificationEventType notificationEventType,
			@JsonProperty("friendMemberId") Long friendMemberId,
			@JsonProperty("friendNickName") String friendNickName,
			@JsonProperty("friendProfileImageUrl") String friendProfileImageUrl,
			@JsonProperty("status") String status
	) {
		super(notificationId, notificationEventType);
		this.friendMemberId = friendMemberId;
		this.friendNickName = friendNickName;
		this.friendProfileImageUrl = friendProfileImageUrl;
		this.status = status;
	}
}
