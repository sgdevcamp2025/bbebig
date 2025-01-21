package com.bbebig.signalingserver.global.response.code;

import com.bbebig.signalingserver.global.response.code.success.BaseCode;
import com.bbebig.signalingserver.global.response.code.success.SuccessStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"code", "message", "result"})
public class ApiResponse<T> {

	private final String code;
	private final String message;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T result;


	public static <T> ApiResponse<T> onSuccess(T result) {
		return new ApiResponse<>(SuccessStatus._OK.getCode(), SuccessStatus._OK.getMessage(), result);
	}

	public static <T> ApiResponse<T> of(BaseCode code, T result) {
		return new ApiResponse<>(code.getReasonHttpStatus().getCode(), code.getReasonHttpStatus().getMessage(), result);
	}

	public static <T> ApiResponse<T> onFailure(String code, String message, T data) {
		return new ApiResponse<>(code, message, data);
	}
}
