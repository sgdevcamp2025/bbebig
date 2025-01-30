package com.bbebig.commonmodule.kafka.dto.notification;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FriendActionEventDto extends NotificationEventDto {

	private Long friendId;

	private String action; // ADD, DELETE, UPDATE

	private String friendNickName;

	private String friendProfileImageUrl;
}
