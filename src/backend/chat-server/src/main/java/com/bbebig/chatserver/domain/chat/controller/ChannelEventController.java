package com.bbebig.chatserver.domain.chat.controller;

import com.bbebig.chatserver.domain.chat.service.MessageProducerService;
import com.bbebig.chatserver.domain.kafka.dto.ChannelEventDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChannelEventController {

	private final MessageProducerService messageProducerService;

	// 현재 보고있는 채널 변경 이벤트 처리
	@MessageMapping("/channel/enter")
	public void enterChannel(@Payload ChannelEventDto channelEventDto) {
		messageProducerService.sendMessageForChannel(channelEventDto);
	}

	// 현재 보고있는 채널 떠남 이벤트 처리
	@MessageMapping("/channel/leave")
	public void leaveChannel(@Payload ChannelEventDto channelEventDto) {
		messageProducerService.sendMessageForChannel(channelEventDto);
	}
}
