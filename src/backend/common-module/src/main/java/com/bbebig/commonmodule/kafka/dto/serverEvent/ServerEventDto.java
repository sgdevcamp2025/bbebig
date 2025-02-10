package com.bbebig.commonmodule.kafka.dto.serverEvent;

import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
public abstract class ServerEventDto {

	Long serverId;

	ServerEventType type;  // ServerEventType Enum 참고
}