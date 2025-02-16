package com.bbebig.passportserver.passport.service;

import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import com.bbebig.commonmodule.passport.HmacUtil;
import com.bbebig.commonmodule.proto.PassportProto.Passport;
import com.bbebig.passportserver.passport.dto.JwtResponseDto;
import com.bbebig.passportserver.passport.dto.PassportResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PassportService {

    private final HmacUtil hmacUtil;

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${eas.passport.expiration}")
    private long EXPIRE_SECONDS;

    /**
     * Passport 발급
     */
    public PassportResponseDto issuePassport(String jwt) {
        // 1) JWT 파싱
        JwtResponseDto jwtResponseDto = parseJwt(jwt);
        if (jwtResponseDto.getMemberId() == null) {
            throw new ErrorHandler(ErrorStatus.INVALID_JWT);
        }

        // 2) memberId
        Long memberId = jwtResponseDto.getMemberId();

        // 3) Passport 발급 (HMAC+Protobuf)
        String passport = generatePassport(memberId, "HIGH");

        // 4) JSON 응답
        return PassportResponseDto.convertToIssuePassportResponse(passport);
    }

    /**
     * 사용자 정보를 받아 Passport 생성 + HMAC 서명 + Base64
     */
    private String generatePassport(long userId, String authLevel) {
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
        return hmacUtil.signPassport(rawBytes);
    }

    /**
     * JWT 파싱
     */
    private JwtResponseDto parseJwt(String jwt) {
        try {
            // 1) Parser 생성
            Jws<Claims> jws = Jwts.parserBuilder()
                    .setSigningKey(jwtSecret.getBytes(StandardCharsets.UTF_8))
                    .build()
                    .parseClaimsJws(jwt);

            // 2) 클레임 추출
            Claims claims = jws.getBody();

            // 3) memberId 추출
            Long memberId = claims.get("id", Integer.class).longValue();

            return JwtResponseDto.convertToJwtResponseDto(memberId);

        } catch (JwtException e) {
            log.warn("[Passport] JWT 파싱 에러: {}", e.getMessage());
            return JwtResponseDto.convertToJwtResponseDto(null);
        }
    }
}
