package com.bbebig.commonmodule.kafka.dto.notification;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper=false)
public class ServerUnreadEventDto extends NotificationEventDto{

	private Long serverId;

	private Long channelId;

	private String status; // UNREAD

	@JsonCreator
	public ServerUnreadEventDto(
			@JsonProperty("memberId") Long memberId,
			@JsonProperty("type") NotificationEventType type,
			@JsonProperty("serverId") Long serverId,
			@JsonProperty("channelId") Long channelId,
			@JsonProperty("status") String status
	) {
		super(memberId, type);
		this.serverId = serverId;
		this.channelId = channelId;
		this.status = status;
	}
}
