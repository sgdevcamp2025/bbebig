package com.bbebig.apigatewayserver.global.response.code.error;

public interface BaseErrorCode {

    public ErrorReasonDTO getReason();

    public ErrorReasonDTO getReasonHttpStatus();
}
