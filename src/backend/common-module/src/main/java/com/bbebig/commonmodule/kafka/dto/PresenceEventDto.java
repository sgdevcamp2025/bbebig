package com.bbebig.commonmodule.kafka.dto;

import com.bbebig.commonmodule.kafka.dto.model.PresenceType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PresenceEventDto {
	private Long memberId;
	private PresenceType globalStatus;
	private PresenceType actualStatus;
	private LocalDateTime lastActivityTime;
}
