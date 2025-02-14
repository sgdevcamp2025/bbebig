package com.bbebig.chatserver.domain.chat.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
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

	@JsonCreator
	public TypingEventDto(@JsonProperty("memberId") Long memberId,
						  @JsonProperty("type") String type,
						  @JsonProperty("memberNickname") String memberNickname,
						  @JsonProperty("channelId") Long channelId,
						  @JsonProperty("serverId") Long serverId) {
		this.memberId = memberId;
		this.type = type;
		this.memberNickname = memberNickname;
		this.channelId = channelId;
		this.serverId = serverId;
	}
}
