package com.bbebig.commonmodule.global.response.exception;

import org.springframework.http.HttpStatus;

/**
 * 인터페이스 BaseExceptionType
 * 예외의 세부 정보를 제공하는 메서드를 정의.
 */
public interface BaseExceptionType {

    int getErrorCode();

    HttpStatus getHttpStatus();

    String getErrorMessage();
}