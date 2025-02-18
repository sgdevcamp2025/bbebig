package com.bbebig.chatserver.domain.chat.handler;

import com.bbebig.chatserver.domain.chat.client.AuthClient;
import com.bbebig.chatserver.domain.chat.dto.response.AuthResponseDto;
import com.bbebig.chatserver.domain.chat.repository.SessionManager;
import com.bbebig.chatserver.domain.chat.service.KafkaProducerService;
import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.kafka.dto.ConnectionEventDto;
import com.bbebig.commonmodule.kafka.dto.model.ChannelType;
import com.bbebig.commonmodule.kafka.dto.model.ConnectionEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.Objects;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class StompHandler implements ChannelInterceptor {

	private final WebApplicationContext webApplicationContext;
	private final SessionManager sessionManager;
	private final AuthClient authClient;
	private final KafkaProducerService kafkaProducerService;

	@Value("${spring.cloud.client.ip-address}")
	private String serverIp;

	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {

		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
		String sessionId = headerAccessor.getSessionId(); // STOMP 세션 ID
		String serverPort = webApplicationContext.getEnvironment().getProperty("local.server.port"); // 서버 포트

		// STOMP Native 헤더에서 플랫폼 / 탭ID / 현재룸정보 추출
		// 클라이언트(브라우저, 앱)에서 connect() 시 아래 헤더를 전송했다고 가정
		String platform = Optional.ofNullable(headerAccessor.getFirstNativeHeader("Platform"))
				.orElse("WEB");
		String deviceType = Optional.ofNullable(headerAccessor.getFirstNativeHeader("DeviceType"))
				.orElse("NONE");
		String currentRoomType = Optional.ofNullable(headerAccessor.getFirstNativeHeader("RoomType"))
				.orElse(null);
		String currentChannelId = Optional.ofNullable(headerAccessor.getFirstNativeHeader("ChannelId"))
				.orElse(null);
		String currentServerId = Optional.ofNullable(headerAccessor.getFirstNativeHeader("ServerId"))
				.orElse(null);

		// CONNECT 요청
		if (StompCommand.CONNECT == headerAccessor.getCommand()) {
			Long memberId = Long.parseLong(Objects.requireNonNull(headerAccessor.getFirstNativeHeader("MemberId")));
//			Optional<String> accessToken = extractBearerToken(headerAccessor);
//			if (accessToken.isEmpty()) {
//				log.error("[Chat] StompHandler: 토큰 정보 없음");
//				throw new MessageDeliveryException(ErrorStatus._UNAUTHORIZED.getMessage());
//			}

//
//			// 2) Auth 서버로 검증
//			AuthResponseDto authResponseDto = authClient.verifyToken(accessToken.get());
//			if (authResponseDto == null || !authResponseDto.getCode().equals("AUTH108")) {
//				log.error("[Chat] StompHandler: 토큰 검증 실패");
//				throw new MessageDeliveryException(ErrorStatus._FORBIDDEN.getMessage());
//			}

//			Long memberId = authResponseDto.getResult().getMemberId();
//			if (memberId != authResponseDto.getResult().getMemberId()) {
//				log.error("[Chat] StompHandler: 토큰 정보와 사용자 정보 불일치");
//				throw new MessageDeliveryException(ErrorStatus._UNAUTHORIZED.getMessage());
//			}

			// 세션 매니저에 (sessionId -> memberId) 저장
			sessionManager.saveConnectSessionInfo(sessionId, memberId);
			log.info("[Chat] StompHandler: CONNECT - memberId={}, sessionId={}, platform={}, roomType={}, channelId={}, serverId={}",
					memberId, sessionId, platform, currentRoomType, currentChannelId, currentServerId);

			ConnectionEventDto connectionEventDto = ConnectionEventDto.builder()
					.memberId(memberId)
					.type(ConnectionEventType.CONNECT)
					.socketSessionId(sessionId)
					.connectedServerIp(serverIp + ":" + serverPort)
					.platform(platform)
					.deviceType(deviceType)
					.currentChannelType(currentRoomType != null ? ChannelType.valueOf(currentRoomType): null)
					.currentChannelId(currentChannelId != null ? Long.parseLong(currentChannelId): null)
					.currentServerId(currentServerId != null ? Long.parseLong(currentServerId): null)
					.build();

			kafkaProducerService.sendMessageForSession(connectionEventDto);

		}
		// DISCONNECT 요청
		else if (StompCommand.DISCONNECT == headerAccessor.getCommand()) {
			Long memberId = sessionManager.findMemberIdBySessionId(sessionId);

			// 세션 매니저에서 제거
			sessionManager.deleteConnectSessionInfo(sessionId, memberId);
			log.info("[Chat] StompHandler: DISCONNECT - memberId={}, sessionId={}, platform={}, deviceType={}",
					memberId, sessionId, platform, deviceType);

			ConnectionEventDto connectionEventDto = ConnectionEventDto.builder()
					.memberId(memberId)
					.type(ConnectionEventType.DISCONNECT)
					.socketSessionId(sessionId)
					.connectedServerIp(serverIp + ":" + serverPort)
					.platform(platform)
					.deviceType(deviceType)
					.build();

			kafkaProducerService.sendMessageForSession(connectionEventDto);
		}
		headerAccessor.getSessionAttributes().put("simpSessionId", sessionId);
		return MessageBuilder
				.withPayload(message.getPayload())
				.setHeader("sessionId", sessionId)
				.build();
	}

	// Native 헤더 "Authorization: Bearer <token>" 추출
	private Optional<String> extractBearerToken(StompHeaderAccessor headerAccessor) {
		return Optional.ofNullable(headerAccessor.getFirstNativeHeader("Authorization"))
				.filter(token -> token.startsWith("Bearer "))
				.map(token -> token.substring("Bearer ".length()));
	}
}
