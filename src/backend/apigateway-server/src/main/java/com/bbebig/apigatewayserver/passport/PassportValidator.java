package com.bbebig.apigatewayserver.passport;

import com.bbebig.apigatewayserver.global.response.code.error.ErrorStatus;
import com.bbebig.apigatewayserver.global.response.exception.ErrorHandler;
import com.bbebig.common.proto.PassportProto.Passport;
import java.time.Instant;
import org.springframework.stereotype.Service;

/**
 * Passport를 검증하는 서비스 클래스
 */
@Service
public class PassportValidator {

    /**
     * Base64로 인코딩된 Passport + HMAC -> decode -> verify -> parse
     */
    public void validatePassport(String base64Passport) {
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
        } catch (Exception e) {
            throw new ErrorHandler(ErrorStatus.PASSPORT_INVALID_OR_TAMPERED);
        }
    }
}
