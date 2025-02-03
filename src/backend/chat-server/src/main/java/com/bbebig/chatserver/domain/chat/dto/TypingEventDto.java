package com.bbebig.chatserver.domain.chat.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TypingEventDto {

	private Long memberId;

	private String type; // TYPING_START

	private String memberNickname;

	private Long channelId;

	// 서버 내부인 경우
	private Long serverId;
}
