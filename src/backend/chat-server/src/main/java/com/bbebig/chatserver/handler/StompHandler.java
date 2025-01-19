package com.bbebig.chatserver.handler;

import com.bbebig.chatserver.apiPayload.code.status.ErrorStatus;
import com.bbebig.chatserver.repository.RedisSessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

	private final RedisSessionManager redisSessionManager;

	// WebSocket을 통해 들어온 요청이 처리 되기 전에 실행
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {

		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
		String sessionId = headerAccessor.getSessionId(); // 세션 ID

		// CONNECT, SUBSCRIBE 요청과 같이 인증이 필요한 요청일 경우
		if (StompCommand.CONNECT == headerAccessor.getCommand() || StompCommand.SUBSCRIBE.equals(headerAccessor.getCommand())) {

			Optional<String> accessToken = extractBearerToken(headerAccessor);

			if (accessToken.isEmpty()) {
				log.error("Stomp Handler : 유효하지 않은 토큰");
				throw new MessageDeliveryException(ErrorStatus._UNAUTHORIZED.getMessage());
			}

			// 토큰이 유효한지 확인하는 로직

			// 토큰이 유효하지 않은 경우

			// 토큰이 유효한 경우

			// simpSessionId를 이용하여 사용자 정보 저장
			String simpSessionId = headerAccessor.getSessionId();

		} else if (StompCommand.DISCONNECT == headerAccessor.getCommand()) { // DISCONNECT 요청일 경우

			// 사용자 정보 삭제
		}
		return message;
	}

	private Optional<String> extractBearerToken(StompHeaderAccessor headerAccessor) {
		return Optional.ofNullable(headerAccessor.getFirstNativeHeader("Authorization"))
				.filter(token -> token.startsWith("Bearer "))
				.map(token -> token.substring("Bearer ".length()));
	}
}
