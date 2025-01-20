package com.bbebig.chatserver.handler;

import com.bbebig.chatserver.client.AuthClient;
import com.bbebig.chatserver.dto.SessionEventDto;
import com.bbebig.chatserver.dto.response.AuthResponseDto;
import com.bbebig.chatserver.global.response.code.error.ErrorStatus;
import com.bbebig.chatserver.repository.RedisSessionManager;
import com.bbebig.chatserver.service.MessageProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Optional;


@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

	@Value("${spring.cloud.client.ip-address}")
	private String serverIp;

	@Value("${server.port}")
	private String serverPort;

	private final RedisSessionManager redisSessionManager;
	private final AuthClient authClient;
	private final MessageProducerService messageProducerService;

	// WebSocket을 통해 들어온 요청이 처리 되기 전에 실행
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {

		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
		String sessionId = headerAccessor.getSessionId(); // 세션 ID

		// CONNECT 요청일 경우
		if (StompCommand.CONNECT == headerAccessor.getCommand()) {
			Optional<String> accessToken = extractBearerToken(headerAccessor);

			if (accessToken.isEmpty()) {
				log.error("[Chat] Stomp Handler : 토큰 정보 없음");
				throw new MessageDeliveryException(ErrorStatus._UNAUTHORIZED.getMessage());
			}

			// AuthClient를 통해 토큰 검증
			AuthResponseDto authResponseDto = authClient.verifyToken(accessToken.get());
			if (authResponseDto == null || authResponseDto.getCode() != 200) {
				log.error("[Chat] Stomp Handler : 토큰 검증 실패");
				throw new MessageDeliveryException(ErrorStatus._FORBIDDEN.getMessage());
			}

			// simpSessionId를 이용하여 사용자 정보 저장
			String simpSessionId = headerAccessor.getSessionId();
			redisSessionManager.saveConnectSessionInfoToRedis(simpSessionId, authResponseDto.getResult().getMemberId());
			log.info("[Chat] Stomp Handler : 사용자 연결 - memberId : {}, sessionId : {}", authResponseDto.getResult().getMemberId(), simpSessionId);

			SessionEventDto sessionEventDto = SessionEventDto.builder()
					.memberId(authResponseDto.getResult().getMemberId())
					.type("CONNECT")
					.currentStatus("ONLINE")
					.socketSessionId(simpSessionId)
					.connectedServerIp(serverIp + ":" + serverPort)
					.build();
			messageProducerService.sendMessageForSession(sessionEventDto);

		} else if (StompCommand.DISCONNECT == headerAccessor.getCommand()) { // DISCONNECT 요청일 경우
			String simpSessionId = headerAccessor.getSessionId();
			Long memberId = redisSessionManager.findMemberIdBySessionId(simpSessionId);
			// 사용자 정보 삭제
			redisSessionManager.deleteConnectSessionInfoToRedis(simpSessionId, memberId);
			log.info("[Chat] Stomp Handler : 사용자 연결 해제 - memberId : {}, sessionId : {}", memberId, simpSessionId);

			SessionEventDto sessionEventDto = SessionEventDto.builder()
					.memberId(memberId)
					.type("DISCONNECT")
					.currentStatus("OFFLINE")
					.socketSessionId(simpSessionId)
					.connectedServerIp(serverIp + ":" + serverPort)
					.build();
			messageProducerService.sendMessageForSession(sessionEventDto);
		}
		return message;
	}

	// 사용 여부 고려해야함
//	@EventListener
//	public void handleWebSocketConnectionListener(SessionConnectedEvent event) {
//		log.info("사용자 입장");
//	}
//
//	@EventListener
//	public void handleWebSocketDisconnectionListener(SessionDisconnectEvent event) {
//		log.info("사용자 퇴장");
//	}

	private Optional<String> extractBearerToken(StompHeaderAccessor headerAccessor) {
		return Optional.ofNullable(headerAccessor.getFirstNativeHeader("Authorization"))
				.filter(token -> token.startsWith("Bearer "))
				.map(token -> token.substring("Bearer ".length()));
	}

}
