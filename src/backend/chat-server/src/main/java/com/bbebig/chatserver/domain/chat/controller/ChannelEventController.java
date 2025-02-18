package com.bbebig.chatserver.domain.chat.controller;

import com.bbebig.chatserver.domain.chat.service.KafkaProducerService;
import com.bbebig.commonmodule.kafka.dto.ChannelEventDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChannelEventController {

	private final KafkaProducerService kafkaProducerService;

	// 현재 보고있는 채널 변경 이벤트 처리
	@MessageMapping("/channel/enter")
	public void enterChannel(@Valid @Payload ChannelEventDto channelEventDto, @Header("sessionId") String sessionId) {
		validateTimestamps(channelEventDto);
		log.info("[Chat] ChannelEventController: 채널 입장 이벤트. memberId = {}, channelId = {}, sessionId = {]", channelEventDto.getMemberId(), channelEventDto.getChannelId(), sessionId);
		kafkaProducerService.sendMessageForChannel(channelEventDto);
	}

	// 현재 보고있는 채널 떠남 이벤트 처리
	@MessageMapping("/channel/leave")
	public void leaveChannel(@Valid @Payload ChannelEventDto channelEventDto, @Header("simpSessionId") String sessionId) {
		validateTimestamps(channelEventDto);
		log.info("[Chat] ChannelEventController: 채널 떠남 이벤트. memberId = {}, channelId = {}, sessionId = {]", channelEventDto.getMemberId(), channelEventDto.getChannelId(), sessionId);
		kafkaProducerService.sendMessageForChannel(channelEventDto);
	}

	public void validateTimestamps(ChannelEventDto channelEventDto) {
		// eventTime 검증
		if (channelEventDto.getEventTime() == null || channelEventDto.getEventTime().isAfter(LocalDateTime.now())) {
			log.warn("[Chat] ChannelEventController: eventTime 값이 유효하지 않아 기본값으로 설정. memberId: {}, received: {}", channelEventDto.getMemberId(), channelEventDto.getEventTime());
			channelEventDto.setEventTime(LocalDateTime.now());
		}
	}
}
