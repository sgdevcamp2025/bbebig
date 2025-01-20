package com.bbebig.chatserver.client;

import com.bbebig.chatserver.dto.response.AuthResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AuthClient {

	private final WebClient webClient;

	@Value("${auth.server.url}")
	private String authServerUrl;

	public AuthClient(WebClient.Builder webClientBuilder) {
		this.webClient = webClientBuilder.baseUrl(authServerUrl).build();
	}

	public AuthResponseDto verifyToken(String token) {
		return webClient.post()
				.uri("/verify-token")
				.header("Authorization", "Bearer " + token)
				.retrieve()
				.bodyToMono(AuthResponseDto.class)
				.onErrorResume(e -> {
					e.printStackTrace();
					return Mono.empty();
				})
				.block(); // 동기 방식으로 실행
	}

}
