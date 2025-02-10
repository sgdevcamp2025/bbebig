package com.bbebig.chatserver.domain.chat.handler;

import com.bbebig.chatserver.domain.chat.client.AuthClient;
import com.bbebig.chatserver.domain.chat.repository.SessionManager;
import com.bbebig.chatserver.domain.chat.service.KafkaProducerService;
import com.bbebig.commonmodule.kafka.dto.ConnectionEventDto;
import com.bbebig.commonmodule.kafka.dto.model.ChannelType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
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

	private final WebApplicationContext webApplicationContext;
	private final SessionManager sessionManager;
	private final AuthClient authClient;
//	private final MemberClient memberClient;
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
		String deviceType = Optional.ofNullable(headerAccessor.getFirstNativeHeader("Device-Type"))
				.orElse("NONE");
		String currentRoomType = Optional.ofNullable(headerAccessor.getFirstNativeHeader("Room-Type"))
				.orElse(null);
		String currentChannelId = Optional.ofNullable(headerAccessor.getFirstNativeHeader("Channel-Id"))
				.orElse(null);
		String currentServerId = Optional.ofNullable(headerAccessor.getFirstNativeHeader("Server-Id"))
				.orElse(null);

		// CONNECT 요청
		if (StompCommand.CONNECT == headerAccessor.getCommand()) {
			// 1) 토큰 추출
//			Optional<String> accessToken = extractBearerToken(headerAccessor);
//			if (accessToken.isEmpty()) {
//				log.error("[Chat] StompHandler: 토큰 정보 없음");
//				throw new MessageDeliveryException(ErrorStatus._UNAUTHORIZED.getMessage());
//			}


//			// 2) Auth 서버로 검증
//			AuthResponseDto authResponseDto = authClient.verifyToken(accessToken.get());
//			if (authResponseDto == null || authResponseDto.getCode() != 200) {
//				log.error("[Chat] StompHandler: 토큰 검증 실패");
//				throw new MessageDeliveryException(ErrorStatus._FORBIDDEN.getMessage());
//			}
//
//			Long memberId = authResponseDto.getResult().getMemberId();
//			if (memberId != authResponseDto.getResult().getMemberId()) {
//				log.error("[Chat] StompHandler: 토큰 정보와 사용자 정보 불일치");
//				throw new MessageDeliveryException(ErrorStatus._UNAUTHORIZED.getMessage());
//			}

			Long memberId = 1L;

			// 세션 매니저에 (sessionId -> memberId) 저장
			sessionManager.saveConnectSessionInfo(sessionId, memberId);
			log.info("[Chat] StompHandler: CONNECT - memberId={}, sessionId={}, platform={}, roomType={}, channelId={}, serverId={}",
					memberId, sessionId, platform, currentRoomType, currentChannelId, currentServerId);

//			MemberResponseDto memberInfo = memberClient.getMemberInfo(memberId);
//			if (memberInfo == null || memberInfo.getCode() != 200) {
//				log.error("[Chat] StompHandler: 사용자 정보 조회 실패");
//				throw new MessageDeliveryException(ErrorStatus.MEMBER_NOT_FOUND.getMessage());
//			}



			ConnectionEventDto connectionEventDto = ConnectionEventDto.builder()
					.memberId(memberId)
					.type("CONNECT")
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
					.type("DISCONNECT")
					.socketSessionId(sessionId)
					.connectedServerIp(serverIp + ":" + serverPort)
					.platform(platform)
					.deviceType(deviceType)
					.build();

			kafkaProducerService.sendMessageForSession(connectionEventDto);
		}

		return message;
	}

	// Native 헤더 "Authorization: Bearer <token>" 추출
	private Optional<String> extractBearerToken(StompHeaderAccessor headerAccessor) {
		return Optional.ofNullable(headerAccessor.getFirstNativeHeader("Authorization"))
				.filter(token -> token.startsWith("Bearer "))
				.map(token -> token.substring("Bearer ".length()));
	}
}
