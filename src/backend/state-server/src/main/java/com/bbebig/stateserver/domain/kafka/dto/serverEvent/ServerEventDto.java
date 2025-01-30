package com.bbebig.chatserver.domain.kafka.dto.serverEvent;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public abstract class ServerEventDto {

	Long serverId;

	String type;  // ServerEventType Enum 참고
}