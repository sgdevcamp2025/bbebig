package com.bbebig.chatserver.domain.chat.controller;

import com.bbebig.chatserver.domain.chat.service.MessageProducerService;
import com.bbebig.chatserver.global.util.SnowflakeGenerator;
import com.bbebig.commonmodule.kafka.dto.ChatMessageDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
public class DmChatController {

	private final MessageProducerService messageProducerService;

	private final SnowflakeGenerator snowflakeGenerator;

	@MessageMapping("/dm/message")
	public void sendChannelMessage(@Valid @Payload ChatMessageDto messageDto) {
		if (messageDto.getTargetMemberIds() == null || messageDto.getTargetMemberIds().isEmpty()) {
			log.error("[Chat] DmChatController: 대상 멤버 ID 없음");
			return;
		}

		messageDto.setId(snowflakeGenerator.nextId());
		validateTimestamps(messageDto);
		
		log.info("[Chat] DmChatController: DM 채팅 메시지 전송. id = {}, senderId = {}, type = {}, messageType = {}", messageDto.getId(), messageDto.getSendMemberId(), messageDto.getType(), messageDto.getMessageType());
		messageProducerService.sendMessageForDmChat(messageDto);
	}

	private void validateTimestamps(ChatMessageDto messageDto) {
		// createdAt 검증
		if (messageDto.getCreatedAt() == null || messageDto.getCreatedAt().isAfter(LocalDateTime.now())) {
			log.warn("[Chat] DmChatController: createdAt 값이 유효하지 않아 기본값으로 설정. messageId: {}, received: {}", messageDto.getId(), messageDto.getCreatedAt());
			messageDto.setCreatedAt(LocalDateTime.now());
		}

		// updatedAt 검증
		if (messageDto.getUpdatedAt() != null && messageDto.getUpdatedAt().isAfter(LocalDateTime.now())) {
			log.warn("[Chat] DmChatController: updatedAt 값이 미래 시간으로 설정되어 기본값으로 변경. messageId: {}, received: {}", messageDto.getId(), messageDto.getUpdatedAt());
			messageDto.setUpdatedAt(LocalDateTime.now());
		}
	}

}
