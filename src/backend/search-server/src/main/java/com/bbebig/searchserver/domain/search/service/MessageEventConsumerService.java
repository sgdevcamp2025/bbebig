package com.bbebig.searchserver.domain.search.service;

import com.bbebig.commonmodule.kafka.dto.ChatMessageDto;
import com.bbebig.commonmodule.kafka.dto.model.ChannelType;
import com.bbebig.searchserver.domain.history.service.HistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageEventConsumerService {

	private final HistoryService historyService;

	/**
	 * 채널 채팅 메시지 이벤트 처리 (CREATE, UPDATE, DELETE)
	 */
	@KafkaListener(topics = "${spring.kafka.topic.channel-chat-event}", groupId = "${spring.kafka.consumer.group-id.channel-chat-event}", containerFactory = "channelChatListener")
	public void consumeForChannelChatEvent(ChatMessageDto chatMessageDto) {
		if (chatMessageDto == null) {
			log.error("[Search] MessageEventConsumerService: 채팅 메시지 정보 없음");
			return;
		}

		if (chatMessageDto.getChannelType() != ChannelType.CHANNEL) {
			log.error("[Chat] MessageEventConsumerService: 채널 채팅 메시지가 아닙니다. ChatMessageDto: {}", chatMessageDto);
			return;
		}

		// 개발용 로그
		log.info("[Chat] MessageEventConsumerService에서 메시지 이벤트 수신: ChatMessageDto: {}", chatMessageDto);

		switch (chatMessageDto.getType()) {
			case "MESSAGE_CREATE":
				historyService.saveChannelMessage(chatMessageDto);
				break;
			case "MESSAGE_UPDATE":
				historyService.updateChannelMessage(chatMessageDto);
				break;
			case "MESSAGE_DELETED":
				historyService.deleteChannelMessage(chatMessageDto.getId());
				break;
			default:
				log.warn("[Chat] MessageEventConsumerService: 처리할 수 없는 메시지 타입. ChatMessageDto: {}", chatMessageDto);
		}
	}

	/**
	 * DM 채팅 메시지 이벤트 처리 (CREATE, UPDATE, DELETE)
	 */
	@KafkaListener(topics = "${spring.kafka.topic.dm-chat-event}", groupId = "${spring.kafka.consumer.group-id.dm-chat-event}", containerFactory = "dmChatListener")
	public void consumeForDmChatEvent(ChatMessageDto chatMessageDto) {
		if (chatMessageDto == null) {
			log.error("[Search] MessageEventConsumerService: 채팅 메시지 정보 없음");
			return;
		}

		if (chatMessageDto.getChannelType() != ChannelType.DM) {
			log.error("[Chat] MessageEventConsumerService: DM 채팅 메시지가 아닙니다. ChatMessageDto: {}", chatMessageDto);
			return;
		}

		switch (chatMessageDto.getType()) {
			case "MESSAGE_CREATE":
				historyService.saveDmMessage(chatMessageDto);
				break;
			case "MESSAGE_UPDATE":
				historyService.updateDmMessage(chatMessageDto);
				break;
			case "MESSAGE_DELETED":
				historyService.deleteDmMessage(chatMessageDto.getId());
				break;
			default:
				log.warn("[Chat] MessageEventConsumerService: 처리할 수 없는 메시지 타입. ChatMessageDto: {}", chatMessageDto);
		}
	}
}
