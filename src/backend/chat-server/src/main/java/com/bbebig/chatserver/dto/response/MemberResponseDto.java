package com.bbebig.chatserver.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MemberResponseDto {

	private int code;
	private String message;
	private Result result;

	@Data
	public static class Result {
		private long memberId;
		private String name;
		private String email;
		private String profileImageUrl;
		private String customPresenceStatus;
		private LocalDateTime lastAccessAt;
	}
}
