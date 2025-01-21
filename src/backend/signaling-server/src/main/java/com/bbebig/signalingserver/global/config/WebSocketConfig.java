package com.bbebig.signalingserver.global.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stream") // WebSocket 접속 시 endpoint 설정
                .setAllowedOriginPatterns("*") // TODO: 배포 후 도메인 설정
                .withSockJS(); // 브라우저에서 WebSocket 을 지원하지 않는 경우에 SockJS 사용
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.setApplicationDestinationPrefixes("/pub/stream"); // Send URL
        registry.enableSimpleBroker("/sub/stream"); // Broker URL
    }
}
