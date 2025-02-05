package com.bbebig.commonmodule.global.response.code.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 일반적인 응답
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "403", "접근 권한이 없는 요청입니다."),
    _LOGIN_FAILURE(HttpStatus.NOT_FOUND, "404", "요청 리소스를 찾을 수 없습니다."),
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "서버 에러, 관리자에게 문의 바랍니다."),
    _SERVICE_UNAVAILABLE_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "503", "서버가 일시적으로 사용중지 되었습니다."),

    // Passport
    PASSPORT_SIGN_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "PASSPORT01", "Passport 서명에 실패했습니다."),
    PASSPORT_DATA_TOO_SHORT(HttpStatus.BAD_REQUEST, "PASSPORT02", "Passport 데이터가 너무 짧습니다."),
    PASSPORT_HMAC_MISMATCH(HttpStatus.UNAUTHORIZED, "PASSPORT03", "HMAC 서명이 일치하지 않습니다."),
    PASSPORT_INVALID_DATA(HttpStatus.BAD_REQUEST, "PASSPORT04", "유효하지 않은 Passport 데이터 또는 Base64 형식입니다."),
    PASSPORT_INVALID_OR_TAMPERED(HttpStatus.BAD_REQUEST, "PASSPORT05", "유효하지 않거나 변조된 Passport 데이터입니다."),
    PASSPORT_EXPIRED(HttpStatus.UNAUTHORIZED, "PASSPORT06", "Passport 토큰이 만료되었습니다."),
    PASSPORT_HEADER_MISSING(HttpStatus.UNAUTHORIZED, "PASSPORT07", "Passport 헤더가 누락되었습니다."),

    // SERVICE
    SERVER_NOT_FOUND(HttpStatus.NOT_FOUND, "SERVICE401", "서버를 찾을 수 없습니다."),
    SERVER_OWNER_FORBIDDEN(HttpStatus.FORBIDDEN, "SERVICE402", "서버장 권한이 없습니다."),
    SERVER_MEMBER_FORBIDDEN(HttpStatus.FORBIDDEN, "SERVICE403", "서버에 속한 멤버가 아닙니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "SERVICE404", "카테고리를 찾을 수 없습니다."),
    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "SERVICE405", "채널을 찾을 수 없습니다."),
    CHANNEL_MEMBER_FORBIDDEN(HttpStatus.FORBIDDEN, "SERVICE406", "채널에 속한 멤버가 아닙니다."),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;


    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .httpStatus(httpStatus)
                .build();
    }
}