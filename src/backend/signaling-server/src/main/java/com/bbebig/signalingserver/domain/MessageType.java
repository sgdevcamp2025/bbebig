package com.bbebig.signalingserver.domain;

public enum MessageType {
    OFFER,
    ANSWER,
    CANDIDATE,
    JOIN_CHANNEL,
    LEAVE_CHANNEL,
    CHANNEL_FULL,   // 채널의 인원이 가득 참
    EXIST_USERS,      // 기존 채널의 유저 정보들
    USER_JOINED,    // 유저가 채널을 입장함
    USER_LEFT,      // 유저가 채널을 퇴장함
    USER_MUTE,      // 유저가 마이크를 음소거함
}
