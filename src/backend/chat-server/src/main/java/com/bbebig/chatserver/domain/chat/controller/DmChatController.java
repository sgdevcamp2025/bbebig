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

@RequiredArgsConstructor
@RestController
@Slf4j
public class DmChatController {

	private final MessageProducerService messageProducerService;

	private final SnowflakeGenerator snowflakeGenerator;

	@MessageMapping("/dm/message")
	public void sendChannelMessage(@Valid @Payload ChatMessageDto messageDto) {
		if (messageDto.getTargetMemberIds() == null || messageDto.getTargetMemberIds().isEmpty()) {
			log.error("[Chat] DmChatController: 대상 멤버 ID 없음");
			return;
		}

		long messageId = snowflakeGenerator.nextId();
		messageDto.setId(messageId);
		log.info("[Chat] DmChatController: DM 채팅 메시지 전송. id = {}, senderId = {}, type = {}, messageType = {}", messageDto.getId(), messageDto.getSendMemberId(), messageDto.getType(), messageDto.getMessageType());
		messageProducerService.sendMessageForDmChat(messageDto);
	}

}
