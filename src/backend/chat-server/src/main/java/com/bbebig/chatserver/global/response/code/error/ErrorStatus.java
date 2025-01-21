package com.bbebig.chatserver.global.response.code.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _BAD_REQUEST(HttpStatus.BAD_REQUEST, "400", "잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "401", "인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "403", "접근 권한이 없는 요청입니다."),
    _LOGIN_FAILURE(HttpStatus.NOT_FOUND, "404", "요청 리소스를 찾을 수 없습니다."),
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "500", "서버 에러, 관리자에게 문의 바랍니다."),
    _SERVICE_UNAVAILABLE_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "503", "서버가 일시적으로 사용중지 되었습니다."),

    //유저 응답
    MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "M401", "유저가 존재하지 않습니다."),

    //채팅 응답
    CHAT_ROOM_NOT_FOUND(HttpStatus.BAD_REQUEST, "C401", "채팅방이 존재하지 않습니다."),
    CHAT_ROOM_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "C402", "이미 존재하는 채팅방입니다."),
    CHAT_ROOM_MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "C403", "채팅방에 참여한 멤버가 존재하지 않습니다.");


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