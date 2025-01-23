package com.bbebig.chatserver.domain.handler;

import com.bbebig.chatserver.domain.client.AuthClient;
import com.bbebig.chatserver.domain.client.MemberClient;
import com.bbebig.chatserver.domain.dto.ConnectionEventDto;
import com.bbebig.chatserver.domain.dto.response.AuthResponseDto;
import com.bbebig.chatserver.domain.dto.response.MemberResponseDto;
import com.bbebig.chatserver.global.response.code.error.ErrorStatus;
import com.bbebig.chatserver.domain.repository.SessionManager;
import com.bbebig.chatserver.domain.service.MessageProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.Optional;


@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

	private WebApplicationContext webApplicationContext;

	@Value("${spring.cloud.client.ip-address}")
	private String serverIp;

	private final SessionManager sessionManager;
	private final AuthClient authClient;
	private final MemberClient memberClient;
	private final MessageProducerService messageProducerService;

	// WebSocket을 통해 들어온 요청이 처리 되기 전에 실행
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {

		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
		String sessionId = headerAccessor.getSessionId(); // 세션 ID
		String serverPort = webApplicationContext.getEnvironment().getProperty("local.server.port"); // 서버 포트

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
			sessionManager.saveConnectSessionInfo(sessionId, authResponseDto.getResult().getMemberId());
			log.info("[Chat] Stomp Handler : 사용자 연결 - memberId : {}, sessionId : {}", authResponseDto.getResult().getMemberId(), sessionId);

			// MemberClient를 통해 사용자 정보 조회
			MemberResponseDto memberInfo = memberClient.getMemberInfo(authResponseDto.getResult().getMemberId());
			if (memberInfo == null || memberInfo.getCode() != 200) {
				log.error("[Chat] Stomp Handler : 사용자 정보 조회 실패");
				throw new MessageDeliveryException(ErrorStatus.MEMBER_NOT_FOUND.getMessage());
			}

			// TODO: 서비스 서버에서 참여중인 서버와 채널 정보를 받아와서, 아래 DTO에 추가해야함

			ConnectionEventDto connectionEventDto = ConnectionEventDto.builder()
					.memberId(memberInfo.getResult().getMemberId())
					.type("CONNECT")
					.socketSessionId(sessionId)
					.connectedServerIp(serverIp + ":" + serverPort)
					.build();
			messageProducerService.sendMessageForSession(connectionEventDto);

		} else if (StompCommand.DISCONNECT == headerAccessor.getCommand()) { // DISCONNECT 요청일 경우
			Long memberId = sessionManager.findMemberIdBySessionId(sessionId);
			// 사용자 정보 삭제
			sessionManager.deleteConnectSessionInfo(sessionId, memberId);
			log.info("[Chat] Stomp Handler : 사용자 연결 해제 - memberId : {}, sessionId : {}", memberId, sessionId);

			ConnectionEventDto connectionEventDto = ConnectionEventDto.builder()
					.memberId(memberId)
					.type("DISCONNECT")
					.socketSessionId(sessionId)
					.connectedServerIp(serverIp + ":" + serverPort)
					.build();
			messageProducerService.sendMessageForSession(connectionEventDto);
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
