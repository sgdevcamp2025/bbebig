package com.bbebig.commonmodule.passport;

import com.bbebig.commonmodule.proto.PassportProto.Passport;
import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;

/**
 * Passport를 검증하는 서비스 클래스
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PassportValidator {

    private final HmacUtil hmacUtil;

    /**
     * Base64로 인코딩된 Passport + HMAC -> decode -> verify -> parse
     */
    public Passport validatePassport(String base64Passport) {
        log.info("[PassportValidator] base64Passport={}", base64Passport);
        try {
            // HMAC 검증 및 원본 데이터 추출
            byte[] rawBytes = hmacUtil.validatePassport(base64Passport);
            log.info("[PassportValidator] after HMAC validation, rawBytes length={}", (rawBytes != null ? rawBytes.length : 0));

            // Protobuf 파싱
            Passport passport = Passport.parseFrom(rawBytes);

            // 만료 시간 검증
            long now = Instant.now().getEpochSecond();
            log.info("[PassportValidator] parsed passport -> memberId={}, expiresAt={}, now={}",
                    passport.getMemberId(), passport.getExpiresAt(), now);

            if (passport.getExpiresAt() < now) {
                throw new ErrorHandler(ErrorStatus.PASSPORT_EXPIRED);
            }

            return passport;
        } catch (Exception e) {
            log.error("[PassportValidator] parse/validate error. base64Passport={}, msg={}", base64Passport, e.getMessage(), e);
            throw new ErrorHandler(ErrorStatus.PASSPORT_INVALID_OR_TAMPERED);
        }
    }
}
