package com.bbebig.searchserver.domain.search.service;


import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import com.bbebig.commonmodule.kafka.dto.serverEvent.*;
import com.bbebig.commonmodule.kafka.dto.serverEvent.status.ServerActionStatus;
import com.bbebig.commonmodule.kafka.dto.serverEvent.status.ServerChannelStatus;
import com.bbebig.searchserver.global.repository.ServerRedisRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ServerEventConsumerService {

	private final ServerRedisRepositoryImpl serverRedisRepositoryImpl;

	@KafkaListener(topics = "${spring.kafka.topic.server-event}", groupId = "${spring.kafka.consumer.group-id.server-event}", containerFactory = "serverEventListener")
	public void consumeForServerEvent(ServerEventDto serverEventDto) {
		if (serverEventDto == null) {
			log.error("[State] ServerEventConsumerService: 서버 이벤트 정보 없음");
			throw new ErrorHandler(ErrorStatus.KAFKA_CONSUME_NULL_EVENT);
		}

		if (serverEventDto.getType().equals(ServerEventType.SERVER_ACTION)) {
			ServerActionEventDto eventDto = (ServerActionEventDto) serverEventDto;
			handleServerActionEvent(eventDto);
		} else if (serverEventDto.getType().equals(ServerEventType.SERVER_CHANNEL)) {
			ServerChannelEventDto eventDto = (ServerChannelEventDto) serverEventDto;
			handleServerChannelEvent(eventDto);
		} else {
			if (!(serverEventDto.getType().equals(ServerEventType.SERVER_CATEGORY) ||
				serverEventDto.getType().equals(ServerEventType.SERVER_MEMBER_ACTION) ||
					serverEventDto.getType().equals(ServerEventType.SERVER_MEMBER_PRESENCE))) {
				log.error("[State] ServerEventConsumerService: 서버 이벤트 타입이 잘못되었습니다. serverEventDto: {}", serverEventDto);
				throw new ErrorHandler(ErrorStatus.INVALID_SERVER_EVENT_TYPE);
			}
		}
	}

	private void handleServerActionEvent(ServerActionEventDto eventDto) {
		Long serverId = eventDto.getServerId();
		// TODO : 추후 서버 액션 이벤트 처리 로직 추가
		if (eventDto.getStatus().equals(ServerActionStatus.DELETE)) {
		} else {
		}

		log.info("[State] ServerEventConsumerService: 서버 액션 이벤트 처리. eventDto: {}", eventDto);
	}

	private void handleServerChannelEvent(ServerChannelEventDto eventDto) {
		if (eventDto.getStatus().equals(ServerChannelStatus.DELETE)) {
			serverRedisRepositoryImpl.deleteChannelMessageList(eventDto.getChannelId());
		}
		log.info("[State] ServerEventConsumerService: 서버 채널 이벤트 처리. eventDto: {}", eventDto);
	}
}
