package com.bbebig.commonmodule.global.response.code;

import com.bbebig.commonmodule.global.response.code.success.BaseCode;
import com.bbebig.commonmodule.global.response.code.success.SuccessStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonPropertyOrder({"code", "message", "result"})
public class CommonResponse<T> {

	private final String code;
	private final String message;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private T result;


	public static <T> CommonResponse<T> onSuccess(T result) {
		return new CommonResponse<>(SuccessStatus._OK.getCode(), SuccessStatus._OK.getMessage(), result);
	}

	public static <T> CommonResponse<T> of(BaseCode code, T result) {
		return new CommonResponse<>(code.getReasonHttpStatus().getCode(), code.getReasonHttpStatus().getMessage(), result);
	}

	public static <T> CommonResponse<T> onFailure(String code, String message, T data) {
		return new CommonResponse<>(code, message, data);
	}
}
