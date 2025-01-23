package com.bbebig.chatserver.domain.dto.response;

import lombok.Data;

@Data
public class AuthResponseDto {

	private int code;
	private String message;
	private Result result;

	@Data
	public static class Result {
		private long memberId;
		private String email;
	}

}
