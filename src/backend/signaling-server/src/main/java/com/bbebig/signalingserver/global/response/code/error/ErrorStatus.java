package com.bbebig.signalingserver.global.response.code.error;

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

    // 다이렉트 스트리밍
    DIRECT_STREAM_RECEIVER_ID_MISSING(HttpStatus.BAD_REQUEST, "400", "수신자 ID(receiverID)가 누락되었습니다."),

    // 그룹 스트리밍
    GROUP_STREAM_INVALID_SIGNAL(HttpStatus.BAD_REQUEST, "400", "잘못된 시그널링 요청입니다."),
    GROUP_STREAM_CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "채널 ID를 찾을 수 없습니다."),
    GROUP_STREAM_SESSION_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "세션 ID에 해당하는 사용자를 찾을 수 없습니다."),
    GROUP_STREAM_ENDPOINT_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "해당 세션에 대한 WebRtcEndpoint를 찾을 수 없습니다."),
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