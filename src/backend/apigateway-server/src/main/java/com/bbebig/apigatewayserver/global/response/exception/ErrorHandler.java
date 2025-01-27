package com.bbebig.apigatewayserver.global.response.exception;


import com.bbebig.apigatewayserver.global.response.code.error.BaseErrorCode;

/**
 * 클래스 ErrorHandler
 * 특정 오류 코드를 기반으로 예외를 처리하는 클래스
 */
public class ErrorHandler extends GeneralException {

    public ErrorHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}