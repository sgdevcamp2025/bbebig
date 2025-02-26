package com.bbebig.chatserver.domain.chat.controller;

import com.bbebig.chatserver.domain.chat.repository.SessionManager;
import com.bbebig.chatserver.domain.chat.service.KafkaProducerService;
import com.bbebig.chatserver.global.util.SequenceRedisGenerator;
import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import com.bbebig.commonmodule.kafka.dto.ChannelEventDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChannelEventController {

	private final KafkaProducerService kafkaProducerService;

	private final ObjectMapper objectMapper;

	private final SequenceRedisGenerator sequenceRedisGenerator;

	// 현재 보고있는 채널 변경 이벤트 처리
	@MessageMapping("/channel/enter")
	public void enterChannel(Message<?> message) throws IOException {
		ChannelEventDto channelEventDto = extractChannelEventDto(message);
		log.info("[Chat] ChannelEventController: 채널 입장 이벤트. memberId = {}, channelId = {}, sessionId = {}",
				channelEventDto.getMemberId(), channelEventDto.getChannelId(), channelEventDto.getSessionId());
		validateTimestamps(channelEventDto);
		kafkaProducerService.sendMessageForChannel(channelEventDto);
	}

	// 현재 보고있는 채널 떠남 이벤트 처리
	@MessageMapping("/channel/leave")
	public void leaveChannel(Message<?> message) throws IOException {
		ChannelEventDto channelEventDto = extractChannelEventDto(message);
		log.info("[Chat] ChannelEventController: 채널 퇴장 이벤트. memberId = {}, channelId = {}, sessionId = {}",
				channelEventDto.getMemberId(), channelEventDto.getChannelId(), channelEventDto.getSessionId());
		validateTimestamps(channelEventDto);

		validateLastInfo(channelEventDto);
		kafkaProducerService.sendMessageForChannel(channelEventDto);
	}

	private void validateTimestamps(ChannelEventDto channelEventDto) {
		// eventTime 검증
		if (channelEventDto.getEventTime() == null || channelEventDto.getEventTime().isAfter(LocalDateTime.now())) {
			log.warn("[Chat] ChannelEventController: eventTime 값이 유효하지 않아 기본값으로 설정. memberId: {}, received: {}", channelEventDto.getMemberId(), channelEventDto.getEventTime());
			channelEventDto.setEventTime(LocalDateTime.now());
		}
	}

	private void validateLastInfo(ChannelEventDto channelEventDto) {
		if (channelEventDto.getLastReadMessageId() == null || channelEventDto.getLastReadSequence() == null) {
			log.warn("[Chat] ChannelEventController: lastReadMessageId 또는 lastReadSequence 값이 null이므로 기본값으로 설정. memberId: {}, channelId: {}", channelEventDto.getMemberId(), channelEventDto.getChannelId());
			Long seq = sequenceRedisGenerator.getSeqForServerChannel(channelEventDto.getChannelId());
			if (seq != null) {
				channelEventDto.setLastReadMessageId(seq);
				channelEventDto.setLastReadSequence(seq);
			} else {
				log.error("[Chat] ChannelEventController: lastReadMessageId 또는 lastReadSequence 값이 null이고 Redis에서 시퀀스를 가져올 수 없음. memberId: {}, channelId: {}", channelEventDto.getMemberId(), channelEventDto.getChannelId());
			}
			channelEventDto.setLastReadMessageId(0L);
			channelEventDto.setLastReadSequence(0L);
		}
	}

	private ChannelEventDto extractChannelEventDto(Message<?> message) throws IOException {
		StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);

		String sessionId = headerAccessor.getSessionId();
		byte[] payloadBytes = (byte[]) message.getPayload();
		ChannelEventDto channelEventDto = objectMapper.readValue(payloadBytes, ChannelEventDto.class);
		channelEventDto.updateSessionId(sessionId);
		return channelEventDto;
	}
}
