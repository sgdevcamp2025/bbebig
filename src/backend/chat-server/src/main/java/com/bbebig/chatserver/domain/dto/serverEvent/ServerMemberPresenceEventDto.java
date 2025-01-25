package com.bbebig.chatserver.domain.dto.serverEvent;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServerMemberPresenceEventDto extends ServerEventDto {

	private Long memberId;

	private String status; // ONLINE, OFFLINE
}
