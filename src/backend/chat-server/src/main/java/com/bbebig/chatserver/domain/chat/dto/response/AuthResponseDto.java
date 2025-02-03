package com.bbebig.chatserver.domain.chat.dto.response;

import lombok.Data;

@Data
public class AuthResponseDto {

	private int code;
	private String message;
	private Result result;

	@Data
	public static class Result {
		private Long memberId;
		private String email;
	}

}
