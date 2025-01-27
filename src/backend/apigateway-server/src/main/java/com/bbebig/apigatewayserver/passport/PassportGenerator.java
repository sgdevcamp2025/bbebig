package com.bbebig.apigatewayserver.passport;

import com.bbebig.common.proto.PassportProto.Passport;
import java.time.Instant;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * Passport를 발급하는 서비스 클래스
 */
@Service
public class PassportGenerator {

    @Value("${eas.passport.expiration}")
    private long EXPIRE_SECONDS;

    /**
     * 사용자 정보를 받아 Passport 생성 + HMAC 서명 + Base64
     */
    public String generatePassport(long userId, String authLevel) {
        long now = Instant.now().getEpochSecond();
        long expire = now + EXPIRE_SECONDS;

        // Protobuf 객체 생성
        Passport passport = Passport.newBuilder()
                .setPassportId(UUID.randomUUID().toString())
                .setMemberId(userId)
                .setIssuedAt(now)
                .setExpiresAt(expire)
                .setAuthLevel(authLevel)
                .build();

        // 직렬화 -> byte[]
        byte[] rawBytes = passport.toByteArray();

        // HMAC 서명 + Base64
        return HmacUtil.signPassport(rawBytes);
    }
}
