package com.bbebig.apigatewayserver.filter;

import com.bbebig.apigatewayserver.dto.JwtValidateResponse;
import com.bbebig.commondomain.passport.PassportExtractor;
import com.bbebig.commondomain.passport.PassportGenerator;
import com.bbebig.commondomain.passport.PassportValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Spring Cloud Gateway에서 모든 요청을 가로채 Passport를 확인하거나,
 * 없으면 Auth 서버로 JWT 검증 후 Passport 발급.
 */
@RequiredArgsConstructor
public class PassportFilter {

    private final PassportExtractor passportExtractor;
    private final PassportValidator passportValidator;
    private final PassportGenerator passportGenerator;

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Value("${auth.server.url}")
    private String authServerUrl;

    @Value("${eas.passport.header}")
    private String PASSPORT_HEADER;

    @Bean
    public GlobalFilter passportFilter() {
        return (exchange, chain) -> {
            String xPassport = passportExtractor.extractPassport(exchange.getRequest().getHeaders());

            if (xPassport != null && !xPassport.isEmpty()) {
                return handleExistingPassport(exchange, chain, xPassport);
            } else {
                return handleMissingPassport(exchange, chain);
            }
        };
    }

    /**
     * CASE 1: 이미 Passport가 있을 경우 검증
     */
    private Mono<Void> handleExistingPassport(ServerWebExchange exchange, GatewayFilterChain chain, String xPassport) {
        try {
            passportValidator.validatePassport(xPassport);
            return chain.filter(exchange);
        } catch (Exception e) {
            return respondWithUnauthorized(exchange);
        }
    }

    /**
     * CASE 2: Passport가 없을 경우 JWT 확인 후 Passport 발급
     */
    private Mono<Void> handleMissingPassport(ServerWebExchange exchange, GatewayFilterChain chain) {
        String authHeader = exchange.getResponse().getHeaders().getFirst(AUTHORIZATION_HEADER);
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            return respondWithUnauthorized(exchange);
        }
        String jwt = authHeader.substring(BEARER_PREFIX.length());
        return validateJwtAndGeneratePassport(exchange, chain, jwt);
    }

    /**
     * Auth 서버로 JWT 검증 및 Passport 발급
     */
    private Mono<Void> validateJwtAndGeneratePassport(ServerWebExchange exchange, GatewayFilterChain chain, String jwt) {
        WebClient webClient = WebClient.create();
        return webClient.get()
                .uri(authServerUrl + "/auth-server/verify-token")
                .header(AUTHORIZATION_HEADER, BEARER_PREFIX + jwt)
                .retrieve()
                .bodyToMono(JwtValidateResponse.class)
                .flatMap(response -> {
                    // 유효하지 않은 토큰일 경우
                    if (!response.isValid()) {
                        return respondWithUnauthorized(exchange);
                    }

                    // 유효한 토큰일 경우 -> Passport 발급
                    String newPassport = passportGenerator.generatePassport(
                            response.getMemberId(),
                            PassportAuthLevel.HIGH.toString()
                    );

                    ServerWebExchange mutatedExchange = exchange.mutate()
                            .request(r -> r.headers(h -> h.set(PASSPORT_HEADER, newPassport)))
                            .build();

                    return chain.filter(mutatedExchange);
                })
                .onErrorResume(ex -> respondWithUnauthorized(exchange));
    }

    /**
     * Unauthorized 응답
     */
    private Mono<Void> respondWithUnauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
