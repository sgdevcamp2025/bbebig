package com.bbebig.chatserver.apiPayload.code;

public interface BaseErrorCode {

    public ErrorReasonDTO getReason();

    public ErrorReasonDTO getReasonHttpStatus();
}
