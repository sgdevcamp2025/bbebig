package com.bbebig.commonmodule.kafka.dto.notification;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class NotificationEventDto {

	Long memberId;

	NotificationEventType type; // NotificationEventType Enum 참고

	@JsonCreator
	public NotificationEventDto(
			@JsonProperty("memberId") Long memberId,
			@JsonProperty("type") NotificationEventType type
	) {
		this.memberId = memberId;
		this.type = type;
	}
}
