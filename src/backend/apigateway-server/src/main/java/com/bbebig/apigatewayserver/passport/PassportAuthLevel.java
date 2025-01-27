package com.bbebig.apigatewayserver.passport;

public enum PassportAuthLevel {
    LOW,        // 신뢰할 수 없는 전송
    HIGH,       // TLS 위의 보안 토큰
    HIGHEST;    // MSL 또는 사용자 자격증명
}
