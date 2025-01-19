package com.bbebig.chatserver.config;

import com.bbebig.chatserver.handler.StompErrorHandler;
import com.bbebig.chatserver.handler.StompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	private final StompHandler stompHandler;
	private final StompErrorHandler stompErrorHandler;

	/**
	 * 메시지 브로커를 구성
	 * 클라이언트가 구독하고 메시지를 받을 경로와 애플리케이션에서 처리할 경로를 설정
	 */
	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		// 클라이언트가 메시지를 받을 경로를 설정
		config.enableSimpleBroker("/sub");
		// 클라이언트가 서버로 메시지를 보낼 때 사용할 경로 설정
		config.setApplicationDestinationPrefixes("/pub");
	}

	/**
	 * 클라이언트가 WebSocket에 연결할 수 있는 엔드포인트를 등록
	 * SockJS를 지원하여 WebSocket을 사용할 수 없는 환경에서도 동작할 수 있도록 함
	 */
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {

		registry.addEndpoint("/ws") // 클라이언트가 연결할 엔드포인트 설정
				.setAllowedOrigins("*");

		// 웹소켓을 사용할 수 없는 환경에서 sockJS 지원
		registry.addEndpoint("/ws/web") // 클라이언트가 연결할 엔드포인트 설정
				.setAllowedOrigins("*")
				.withSockJS();

		registry.setErrorHandler(stompErrorHandler);
	}

	/**
	 * 클라이언트로 들어오는 요청을 처리하기 전에 실행
	 */
	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(stompHandler);
	}
}