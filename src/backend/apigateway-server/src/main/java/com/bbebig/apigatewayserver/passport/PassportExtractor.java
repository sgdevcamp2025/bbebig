package com.bbebig.apigatewayserver.passport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

/**
 * Passport를 헤더에서 추출
 */
@Component
public class PassportExtractor {

    @Value("${eas.passport.header}")
    private static String PASSPORT_HEADER;

    /**
     * WebFlux 방식, 헤더에서 Passport를 추출
     */
    public String extractPassport(HttpHeaders headers) {
        return headers.getFirst(PASSPORT_HEADER);
    }
}
