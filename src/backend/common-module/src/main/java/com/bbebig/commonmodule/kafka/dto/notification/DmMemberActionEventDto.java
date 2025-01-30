package com.bbebig.commonmodule.kafka.dto.notification;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DmMemberActionEventDto extends NotificationEventDto {

	private Long targetMemberId;

	private Long channelId;

	private String targetMemberNickName;

	private String targetMemberProfileImageUrl;

	private String action; // JOIN, LEAVE, UPDATE
}
