package com.bbebig.commonmodule.kafka.dto.notification;

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
}
