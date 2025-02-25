package com.bbebig.pushserver.adapter.in;

import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import com.bbebig.commonmodule.kafka.dto.ChatMessageDto;
import com.bbebig.commonmodule.kafka.dto.model.ChatType;
import com.bbebig.pushserver.application.port.in.ChannelChatEventPort;
import com.bbebig.pushserver.application.service.PushService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class KafkaChannelChatConsumer implements ChannelChatEventPort {

	private final PushService pushService;

	@KafkaListener(topics = "${spring.kafka.topic.channel-chat-event}", groupId = "${spring.kafka.consumer.group-id.channel-chat-event}", containerFactory = "channelChatListener")
	public void consumeForChannelChatEvent(ChatMessageDto chatMessageDto) {
		handleChannelChatEvent(chatMessageDto);
	}

	@Override
	public void handleChannelChatEvent(ChatMessageDto chatMessageDto) {
		if (chatMessageDto == null) {
			log.error("[Chat] MessageEventConsumerService: 채팅 메시지 정보 없음");
			throw new ErrorHandler(ErrorStatus.KAFKA_CONSUME_NULL_EVENT);
		}

		log.info("[Chat] MessageEventConsumerService: 채팅 메시지 수신. ChatMessageDto: {}", chatMessageDto);

		if (chatMessageDto.getChatType() != ChatType.CHANNEL) {
			log.error("[Chat] MessageEventConsumerService: 채널 채팅 메시지가 아닙니다. ChatMessageDto: {}", chatMessageDto);
			throw new ErrorHandler(ErrorStatus.CHAT_TYPE_INVALID);
		}

		pushService.sendUnreadPush(chatMessageDto);
	}
}
