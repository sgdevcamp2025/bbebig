package com.bbebig.apigatewayserver.filter;

import com.bbebig.apigatewayserver.dto.PassportResponseDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * Spring Cloud Gateway에서 모든 요청을 가로채 Passport를 확인하거나,
 * 없으면 Auth 서버로 JWT 검증 후 Passport 발급.
 */
@Component
public class PassportFilter extends AbstractGatewayFilterFactory<Object> {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    private final WebClient webClient = WebClient.create();

    @Value("${eas.passport.header}")
    private String passportHeader;

    @Value("${passport.server.url}")
    private String passportServerUrl;

    public PassportFilter() {
        super(Object.class);
    }

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            String authHeader = exchange.getRequest().getHeaders().getFirst(AUTHORIZATION_HEADER);

            // 1) JWT가 있으면 Passport Server Call
            if (authHeader != null && authHeader.startsWith(BEARER_PREFIX)) {
                String jwt = authHeader.substring(BEARER_PREFIX.length());
                return getPassportFromPassportServer(exchange, chain, jwt);
            }

            // 2) JWT가 없으면 401
            return respondWithUnauthorized(exchange);
        };
    }

    /**
     * JWT를 통해 Passport 발급
     */
    private Mono<Void> getPassportFromPassportServer(ServerWebExchange exchange,
                                                     GatewayFilterChain chain,
                                                     String jwt) {
        return webClient.post()
                .uri(passportServerUrl + "/passports?jwt=" + jwt)
                .retrieve()
                .bodyToMono(PassportResponseDto.class)
                .flatMap(response -> {
                    // Passport 발급 실패
                    if (response.getPassport() == null) {
                        return respondWithUnauthorized(exchange);
                    }

                    // 발급된 Passport를 X-Passport 헤더로 설정
                    ServerWebExchange mutated = exchange.mutate()
                            .request(r -> r.headers(h -> h.set(passportHeader, response.getPassport())))
                            .build();

                    // 다음 필터로 진행
                    return chain.filter(mutated);
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
