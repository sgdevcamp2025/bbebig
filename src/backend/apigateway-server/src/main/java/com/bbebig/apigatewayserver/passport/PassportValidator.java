package com.bbebig.apigatewayserver.passport;

import com.bbebig.apigatewayserver.global.response.code.error.ErrorStatus;
import com.bbebig.apigatewayserver.global.response.exception.ErrorHandler;
import com.bbebig.common.proto.PassportProto.Passport;
import java.time.Instant;
import org.springframework.stereotype.Service;

/**
 * Passport를 검증
 */
@Service
public class PassportValidator {

    /**
     * Base64로 인코딩된 Passport + HMAC -> decode -> verify -> parse
     */
    public Passport validatePassport(String base64Passport) {
        // HMAC 검증 -> raw bytes
        byte[] rawBytes = HmacUtil.validatePassport(base64Passport);

        try {
            // Protobuf 파싱
            Passport passport = Passport.parseFrom(rawBytes);

            // 만료 체크
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
