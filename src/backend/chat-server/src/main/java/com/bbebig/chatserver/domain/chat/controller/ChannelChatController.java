package com.bbebig.chatserver.domain.chat.controller;

import com.bbebig.chatserver.domain.kafka.dto.ChatMessageDto;
import com.bbebig.chatserver.domain.chat.service.MessageProducerService;
import com.bbebig.chatserver.global.util.SnowflakeGenerator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@Slf4j
public class ChannelChatController {

	private final MessageProducerService messageProducerService;

	private final SnowflakeGenerator snowflakeGenerator;

	@MessageMapping("/channel/message")
	public void sendChannelMessage(@Valid @Payload ChatMessageDto messageDto) {
		if (messageDto.getServerId() == null) {
			log.error("[Chat] ChannelChatController: 서버 ID 없음");
			return;
		}

		messageDto.setId(snowflakeGenerator.nextId());
		validateTimestamps(messageDto);

		log.info("[Chat] ChannelChatController: 채널 채팅 메시지 전송. id = {}, channel = {}, senderId = {}, type = {}, messageType = {}", messageDto.getId(), messageDto.getChannelId(), messageDto.getSendMemberId(), messageDto.getType(), messageDto.getMessageType());
		messageProducerService.sendMessageForChannelChat(messageDto);
	}

	private void validateTimestamps(ChatMessageDto messageDto) {
		LocalDateTime now = LocalDateTime.now();

		// createdAt 검증
		if (messageDto.getCreatedAt() == null || messageDto.getCreatedAt().isAfter(now)) {
			log.warn("[Chat] ChannelChatController: createdAt 값이 유효하지 않아 기본값으로 설정. messageId: {}, received: {}", messageDto.getId(), messageDto.getCreatedAt());
			messageDto.setCreatedAt(now);
		}

		// updatedAt 검증
		if (messageDto.getUpdatedAt() != null && messageDto.getUpdatedAt().isAfter(now)) {
			log.warn("[Chat] ChannelChatController: updatedAt 값이 미래 시간으로 설정되어 기본값으로 변경. messageId: {}, received: {}", messageDto.getId(), messageDto.getUpdatedAt());
			messageDto.setUpdatedAt(now);
		}
	}

}
