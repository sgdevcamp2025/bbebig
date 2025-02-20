package com.bbebig.commonmodule.kafka.dto;

import com.bbebig.commonmodule.kafka.dto.model.PresenceType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PresenceEventDto {
	private Long memberId;
	private String type; // MEMBER_PRESENCE_UPDATE
	private PresenceType globalStatus;
	private PresenceType actualStatus;
	private PresenceType customStatus;
	private LocalDateTime lastActivityTime;

	@JsonCreator
	public PresenceEventDto(@JsonProperty("memberId") Long memberId,
							@JsonProperty("type") String type,
							@JsonProperty("globalStatus") PresenceType globalStatus,
							@JsonProperty("actualStatus") PresenceType actualStatus,
							@JsonProperty("customStatus") PresenceType customStatus,
							@JsonProperty("lastActivityTime") LocalDateTime lastActivityTime) {
		this.memberId = memberId;
		this.type = type;
		this.globalStatus = globalStatus;
		this.actualStatus = actualStatus;
		this.customStatus = customStatus;
		this.lastActivityTime = lastActivityTime;
	}
}
