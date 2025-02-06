package com.bbebig.chatserver.domain.chat.handler;

import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

import java.nio.charset.StandardCharsets;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class StompErrorHandler extends StompSubProtocolErrorHandler {

	// 클라이언트 메시지 처리 중에 발생한 오류를 처리
	@Override
	public Message<byte[]> handleClientMessageProcessingError(
			Message<byte[]> clientMessage,
			Throwable ex) {

		if (ErrorStatus._UNAUTHORIZED.getMessage().equals(ex.getMessage())) {
			return errorMessage("유효하지 않은 권한입니다.");
		}

		if (ErrorStatus.CHAT_ROOM_NOT_FOUND.getMessage().equals(ex.getMessage())) {
			return errorMessage("채팅방을 찾을 수 없습니다.");
		}

		return super.handleClientMessageProcessingError(clientMessage, ex);
	}

	// 오류 메시지를 포함한 Message 객체를 생성
	private Message<byte[]> errorMessage(String errorMessage) {

		StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
		accessor.setLeaveMutable(true);

		return MessageBuilder.createMessage(errorMessage.getBytes(StandardCharsets.UTF_8),
				accessor.getMessageHeaders());
	}
}

