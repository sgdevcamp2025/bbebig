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
    CIRCUIT_OPEN_ERROR(HttpStatus.SERVICE_UNAVAILABLE, "503", "서비스가 일시적으로 사용 중지되었습니다."),

    // Passport
    PASSPORT_SIGN_FAIL(HttpStatus.INTERNAL_SERVER_ERROR, "PASSPORT01", "Passport 서명에 실패했습니다."),
    PASSPORT_DATA_TOO_SHORT(HttpStatus.BAD_REQUEST, "PASSPORT02", "Passport 데이터가 너무 짧습니다."),
    PASSPORT_HMAC_MISMATCH(HttpStatus.UNAUTHORIZED, "PASSPORT03", "HMAC 서명이 일치하지 않습니다."),
    PASSPORT_INVALID_DATA(HttpStatus.BAD_REQUEST, "PASSPORT04", "유효하지 않은 Passport 데이터 또는 Base64 형식입니다."),
    PASSPORT_INVALID_OR_TAMPERED(HttpStatus.BAD_REQUEST, "PASSPORT05", "유효하지 않거나 변조된 Passport 데이터입니다."),
    PASSPORT_EXPIRED(HttpStatus.UNAUTHORIZED, "PASSPORT06", "Passport 토큰이 만료되었습니다."),
    PASSPORT_HEADER_MISSING(HttpStatus.UNAUTHORIZED, "PASSPORT07", "Passport 헤더가 누락되었습니다."),
    INVALID_JWT(HttpStatus.BAD_REQUEST, "PASSPORT08", "JWT 파싱에 실패했습니다."),

    // 다이렉트 스트리밍
    DIRECT_STREAM_RECEIVER_ID_MISSING(HttpStatus.BAD_REQUEST, "400", "수신자 ID(receiverID)가 누락되었습니다."),

    // 그룹 스트리밍
    GROUP_STREAM_INVALID_SIGNAL(HttpStatus.BAD_REQUEST, "400", "잘못된 시그널링 요청입니다."),
    GROUP_STREAM_CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "채널 ID를 찾을 수 없습니다."),
    GROUP_STREAM_SESSION_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "세션 ID에 해당하는 사용자를 찾을 수 없습니다."),
    GROUP_STREAM_ENDPOINT_NOT_FOUND(HttpStatus.NOT_FOUND, "404", "해당 세션에 대한 WebRtcEndpoint를 찾을 수 없습니다."),

    // Chat
    CHAT_ROOM_NOT_FOUND(HttpStatus.BAD_REQUEST, "CHAT401", "채팅방이 존재하지 않습니다."),
    CHAT_ROOM_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "CHAT402", "이미 존재하는 채팅방입니다."),
    CHAT_ROOM_MEMBER_NOT_FOUND(HttpStatus.BAD_REQUEST, "CHAT403", "채팅방에 참여한 멤버가 존재하지 않습니다."),
    CHANNEL_LEAVE_LAST_INFO_NOT_VALID(HttpStatus.BAD_REQUEST, "CHAT404", "채널 퇴장 시 마지막 정보가 유효하지 않습니다."),

    // SERVICE
    SERVER_NOT_FOUND(HttpStatus.NOT_FOUND, "SERVICE401", "서버를 찾을 수 없습니다."),
    SERVER_OWNER_FORBIDDEN(HttpStatus.FORBIDDEN, "SERVICE402", "서버장 권한이 없습니다."),
    SERVER_MEMBER_FORBIDDEN(HttpStatus.FORBIDDEN, "SERVICE403", "서버에 속한 멤버가 아닙니다."),
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "SERVICE404", "카테고리를 찾을 수 없습니다."),
    CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "SERVICE405", "채널을 찾을 수 없습니다."),
    CHANNEL_MEMBER_FORBIDDEN(HttpStatus.FORBIDDEN, "SERVICE406", "채널에 속한 멤버가 아닙니다."),
    SERVER_MEMBERS_NOT_FOUND(HttpStatus.NOT_FOUND, "SERVICE407", "서버에 속한 멤버를 찾을 수 없습니다."),
    SERVER_CHANNELS_NOT_FOUND(HttpStatus.NOT_FOUND, "SERVICE408", "서버에 속한 채널을 찾을 수 없습니다."),
    MEMBER_PARTICIPATE_SERVER_NOT_FOUND(HttpStatus.NOT_FOUND, "SERVICE409", "멤버가 참여중인 서버를 찾을 수 없습니다."),
    MEMBER_PARTICIPATED_CHANNEL_NOT_FOUND(HttpStatus.NOT_FOUND, "SERVICE410", "멤버가 참여중인 채널을 찾을 수 없습니다."),
    SERVER_MEMBER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "SERVICE411", "멤버가 이미 서버에 참여 중입니다."),
    CHANNEL_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "SERVICE412", "채널에 속한 멤버를 찾을 수 없습니다."),
    MEMBER_CUSTOM_STATE_GET_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "SERVICE413", "멤버의 커스텀 상태를 받아오는데 실패했습니다."),

    //STATE
    MEMBER_SERVER_LIST_CACHE_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "STATE401", "멤버가 참여중인 서버 목록 캐시에 실패했습니다."),
    MEMBER_ACTUAL_STATE_NOT_FOUND(HttpStatus.NOT_FOUND, "STATE402", "멤버의 실제 상태를 찾을 수 없습니다."),
    MEMBER_GLOBAL_STATE_NOT_FOUND(HttpStatus.NOT_FOUND, "STATE403", "멤버의 전역 상태를 찾을 수 없습니다."),
    MEMBER_PRESENCE_STATE_NOT_FOUND(HttpStatus.NOT_FOUND, "STATE404", "멤버의 접속 상태를 찾을 수 없습니다."),
    SERVER_MEMBER_PRESENCE_STATE_NOT_FOUND(HttpStatus.NOT_FOUND, "STATE405", "서버 멤버의 접속 상태를 찾을 수 없습니다."),

    // SEARCH
    SERVER_LAST_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "SEARCH401", "서버별 마지막 방문 정보를 받아오는데에 실패했습니다."),

    // 카프카 이벤트 관련
    KAFKA_CONSUME_NULL_EVENT(HttpStatus.SERVICE_UNAVAILABLE, "KAFKA00", "카프카 이벤트가 null입니다."),
    INVALID_SERVER_EVENT_TYPE(HttpStatus.SERVICE_UNAVAILABLE, "KAFKA01", "유효하지 않은 서버 이벤트 타입입니다."),
    INVALID_NOTIFICATION_EVENT_TYPE(HttpStatus.SERVICE_UNAVAILABLE, "KAFKA02", "유효하지 않은 알림 이벤트 타입입니다."),
    INVALID_PRESENCE_EVENT_TYPE(HttpStatus.SERVICE_UNAVAILABLE, "KAFKA03", "유효하지 않은 프레즌스 이벤트 타입입니다."),
    INVALID_MESSAGE_EVENT_TYPE(HttpStatus.SERVICE_UNAVAILABLE, "KAFKA04", "유효하지 않은 메시지 이벤트 타입입니다."),
    INVALID_CHANNEL_EVENT_TYPE(HttpStatus.SERVICE_UNAVAILABLE, "KAFKA05", "유효하지 않은 채널 이벤트 타입입니다."),

    // MEMBER
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER401", "멤버를 찾을 수 없습니다."),
    FRIEND_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER402", "친구를 찾을 수 없습니다."),
    FRIEND_RELATION_EXIST(HttpStatus.BAD_REQUEST, "MEMBER403", "이미 친구 관계이거나 요청 중인 상태입니다."),
    FRIEND_RELATION_NOT_EXIST(HttpStatus.BAD_REQUEST, "MEMBER404", "해당 멤버와 친구 관계가 아닙니다."),
    FRIEND_REQUEST_NOT_FOUND(HttpStatus.BAD_REQUEST, "MEMBER405", "친구 요청 상태가 아닙니다."),
    FRIEND_REQUEST_NOT_PENDING(HttpStatus.BAD_REQUEST, "MEMBER406", "친구 요청 상태가 아닙니다."),
    UNAUTHORIZED_FRIEND_ACTION(HttpStatus.FORBIDDEN, "MEMBER407", "본인의 친구 관계가 아닙니다."),
    FRIEND_REQUEST_SELF(HttpStatus.BAD_REQUEST, "MEMBER408", "자기 자신에게 친구 요청을 보낼 수 없습니다."),

    // SEARCH
    CANNOT_MODIFY_DELETED_MESSAGE(HttpStatus.BAD_REQUEST, "SEARCH401", "삭제된 메시지는 수정할 수 없습니다."),
    CHAT_MESSAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "SEARCH402", "채팅 메시지를 찾을 수 없습니다."),
    MESSAGE_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "SEARCH403", "이미 삭제된 메시지입니다."),
    SEARCH_OPTION_INVALID(HttpStatus.BAD_REQUEST, "SEARCH404", "검색 옵션을 확인해주세요."),
    CHAT_TYPE_INVALID(HttpStatus.BAD_REQUEST, "SEARCH405", "채팅 타입을 확인해주세요."),
    CHANNEL_LAST_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "SEARCH406", "채널 마지막 정보를 찾을 수 없습니다.")
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