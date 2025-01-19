package com.bbebig.chatserver.apiPayload.exception;

import com.bbebig.chatserver.apiPayload.code.BaseErrorCode;
import com.bbebig.chatserver.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 클래스 GeneralException
 * 기본 예외 처리를 위한 클래스
 */
@Getter
@AllArgsConstructor
public class GeneralException extends RuntimeException {

    private final BaseErrorCode code;

    public ErrorReasonDTO getErrorReason() {
        return this.code.getReason();
    }

    public ErrorReasonDTO getErrorReasonHttpStatus() {
        return this.code.getReasonHttpStatus();
    }
}
