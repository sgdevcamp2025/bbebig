package com.bbebig.commonmodule.passport;

import com.bbebig.commonmodule.proto.PassportProto.Passport;
import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Passport를 검증하는 서비스 클래스
 */
@Component
public class PassportValidator {

    /**
     * Base64로 인코딩된 Passport + HMAC -> decode -> verify -> parse
     */
    public Passport validatePassport(String base64Passport) {
        try {
            // HMAC 검증 및 원본 데이터 추출
            byte[] rawBytes = HmacUtil.validatePassport(base64Passport);

            // Protobuf 파싱
            Passport passport = Passport.parseFrom(rawBytes);

            // 만료 시간 검증
            long now = Instant.now().getEpochSecond();
            if (passport.getExpiresAt() < now) {
                throw new ErrorHandler(ErrorStatus.PASSPORT_EXPIRED);
            }

            return passport;
        } catch (Exception e) {
            throw new ErrorHandler(ErrorStatus.PASSPORT_INVALID_OR_TAMPERED);
        }
    }
}
