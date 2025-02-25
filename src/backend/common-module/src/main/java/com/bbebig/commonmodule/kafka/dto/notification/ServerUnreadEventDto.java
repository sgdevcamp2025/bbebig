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

	private Long sequence;

	private String targetSessionId;

	private String status; // UNREAD

	@JsonCreator
	public ServerUnreadEventDto(
			@JsonProperty("memberId") Long memberId,
			@JsonProperty("type") NotificationEventType type,
			@JsonProperty("serverId") Long serverId,
			@JsonProperty("channelId") Long channelId,
			@JsonProperty("sequence") Long sequence,
			@JsonProperty("targetSessionId") String targetSessionId,
			@JsonProperty("status") String status
	) {
		super(memberId, type);
		this.serverId = serverId;
		this.channelId = channelId;
		this.sequence = sequence;
		this.targetSessionId = targetSessionId;
		this.status = status;
	}
}
