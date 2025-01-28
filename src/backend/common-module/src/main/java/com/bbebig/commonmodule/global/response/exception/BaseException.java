package com.bbebig.commonmodule.global.response.exception;

/**
 * 추상 클래스 BaseException
 * 모든 사용자 정의 예외의 기본 클래스
 * getExceptionType 메서드를 구현
 */
public abstract class BaseException extends RuntimeException {

    public abstract BaseExceptionType getExceptionType();
}