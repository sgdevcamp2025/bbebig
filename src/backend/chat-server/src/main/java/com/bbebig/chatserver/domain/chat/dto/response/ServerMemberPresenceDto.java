package com.bbebig.chatserver.domain.chat.dto.response;

import com.bbebig.commonmodule.kafka.dto.model.PresenceType;
import com.bbebig.commonmodule.kafka.dto.serverEvent.ServerEventType;
import com.bbebig.commonmodule.kafka.dto.serverEvent.ServerMemberPresenceEventDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServerMemberPresenceDto {
	private Long serverId;
	private Long memberId;
	private ServerEventType type; // MEMBER_PRESENCE_UPDATE
	private PresenceType globalStatus;

	public static ServerMemberPresenceDto convertEventToDto(ServerMemberPresenceEventDto event) {
		return ServerMemberPresenceDto.builder()
				.serverId(event.getServerId())
				.memberId(event.getMemberId())
				.type(event.getType())
				.globalStatus(event.getGlobalStatus())
				.build();
	}
}
