package com.bbebig.chatserver.domain.dto.serverEvent;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServerMemberActionEventDto extends ServerEventDto {

	private Long memberId;

	private String nickname;

	private String profileImageUrl;

	private String status; // JOIN, LEAVE, UPDATE

}
