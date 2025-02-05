package com.bbebig.commonmodule.kafka.dto.notification;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper=false)
public class FriendActionEventDto extends NotificationEventDto {

	private Long friendId;

	private String status; // ADD, DELETE, UPDATE

	private String friendNickName;

	private String friendProfileImageUrl;
}
