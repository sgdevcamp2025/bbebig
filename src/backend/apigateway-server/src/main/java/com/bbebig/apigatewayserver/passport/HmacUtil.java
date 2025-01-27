package com.bbebig.apigatewayserver.passport;

import com.bbebig.apigatewayserver.global.response.code.error.ErrorStatus;
import com.bbebig.apigatewayserver.global.response.exception.ErrorHandler;
import java.security.MessageDigest;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.beans.factory.annotation.Value;

/**
 * HMAC 서명 유틸리티 클래스
 */
public class HmacUtil {

    @Value("${eas.passport.secret}")
    private static String SECRET_KEY;

    private static final String HMAC_SHA256 = "HmacSHA256";
    private static final int HMAC_LENGTH = 32;

    /**
     * 원본(직렬화된 Passport) -> HMAC 계산 -> [원본 + HMAC] -> Base64 인코딩
     */
    public static String signPassport(byte[] rawData) {
        try {
            byte[] hmac = computeHmacSHA256(rawData);
            byte[] combined = new byte[rawData.length + hmac.length];

            // 데이터 결합
            System.arraycopy(rawData, 0, combined, 0, rawData.length);
            System.arraycopy(hmac, 0, combined, rawData.length, hmac.length);

            return Base64.getEncoder().encodeToString(combined);
        } catch (Exception e) {
            throw new ErrorHandler(ErrorStatus.PASSPORT_SIGN_FAIL);
        }
    }

    /**
     * Base64 -> [원본 + HMAC] 분리 -> 재계산 -> 검증 -> 원본 바이트 반환
     */
    public static byte[] validatePassport(String base64Passport) {
        try {
            byte[] combined = Base64.getDecoder().decode(base64Passport);

            if (combined.length < HMAC_LENGTH) {
                throw new ErrorHandler(ErrorStatus.PASSPORT_DATA_TOO_SHORT);
            }

            int dataLength = combined.length - HMAC_LENGTH;
            byte[] rawData = new byte[dataLength];
            byte[] hmac = new byte[HMAC_LENGTH];

            // 데이터와 HMAC 분리
            System.arraycopy(combined, 0, rawData, 0, dataLength);
            System.arraycopy(combined, dataLength, hmac, 0, HMAC_LENGTH);

            // HMAC 검증
            byte[] expected = computeHmacSHA256(rawData);
            if (!MessageDigest.isEqual(expected, hmac)) {
                throw new ErrorHandler(ErrorStatus.PASSPORT_HMAC_MISMATCH);
            }

            return rawData;
        } catch (Exception e) {
            throw new ErrorHandler(ErrorStatus.PASSPORT_INVALID_DATA);
        }
    }

    /**
     * HMAC-SHA256 계산 (32byte)
     */
    private static byte[] computeHmacSHA256(byte[] data) throws Exception {
        Mac mac = Mac.getInstance(HMAC_SHA256);
        mac.init(new SecretKeySpec(SECRET_KEY.getBytes(), HMAC_SHA256));
        return mac.doFinal(data);
    }
}
