package com.bbebig.serviceserver.global.kafka;


import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import com.bbebig.commonmodule.kafka.dto.ChannelEventDto;
import com.bbebig.commonmodule.kafka.dto.model.ChannelEventType;
import com.bbebig.commonmodule.redis.domain.ChannelLastInfo;
import com.bbebig.commonmodule.redis.domain.ServerLastInfo;
import com.bbebig.serviceserver.channel.entity.ChannelMember;
import com.bbebig.serviceserver.channel.repository.ChannelMemberRepository;
import com.bbebig.serviceserver.channel.service.ChannelService;
import com.bbebig.serviceserver.server.entity.ServerMember;
import com.bbebig.serviceserver.server.repository.MemberRedisRepositoryImpl;
import com.bbebig.serviceserver.server.repository.ServerMemberRepository;
import com.bbebig.serviceserver.server.service.ServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelEventConsumerService {

	private final ChannelMemberRepository channelMemberRepository;
	private final ServerMemberRepository serverMemberRepository;
	private final MemberRedisRepositoryImpl memberRedisRepositoryImpl;

	private final ChannelService channelService;
	private final ServerService serverService;

	@KafkaListener(topics = "${spring.kafka.topic.channel-event}", groupId = "${spring.kafka.consumer.group-id.channel-event}", containerFactory = "channelEventListener")
	public void consumeForChannelEvent(ChannelEventDto channelEventDto) {
		if (channelEventDto == null) {
			throw new ErrorHandler(ErrorStatus.KAFKA_CONSUME_NULL_EVENT);
		}

		// 개발용 로그
		log.info("[State] ChannelEventConsumerService: 채널 이벤트 수신. memberId: {}, type: {}, dto: {}", channelEventDto.getMemberId(), channelEventDto.getType(), channelEventDto);
		if (channelEventDto.getType().equals(ChannelEventType.CHANNEL_ENTER) || channelEventDto.getType().equals(ChannelEventType.CHANNEL_LEAVE)) {
			handleChannelEvent(channelEventDto);
		} else {
			throw new ErrorHandler(ErrorStatus.INVALID_CHANNEL_EVENT_TYPE);
		}
	}

	private void handleChannelEvent(ChannelEventDto channelEventDto) {
		// TODO : 예외 처리 어떻게 할지. ex) 레디스 캐싱정보 토대로 확인 등
		Long memberId = channelEventDto.getMemberId();
		Long channelId = channelEventDto.getChannelId();

		ServerMember serverMember = serverMemberRepository.findByMemberIdAndServerId(channelEventDto.getMemberId(), channelEventDto.getServerId())
				.orElseThrow(() -> new ErrorHandler(ErrorStatus.SERVER_MEMBERS_NOT_FOUND));

		ChannelMember channelMember = channelMemberRepository.findByServerMemberIdAndChannelId(serverMember.getId(), channelId)
				.orElseThrow(() -> new ErrorHandler(ErrorStatus.CHANNEL_MEMBER_NOT_FOUND));

		channelMember.updateLastInfo(channelEventDto.getLastReadMessageId(), channelEventDto.getLastReadSequence(),channelEventDto.getEventTime());

		if (memberRedisRepositoryImpl.existsServerLastInfo(memberId, channelEventDto.getServerId())) {
			ServerLastInfo serverLastInfo = memberRedisRepositoryImpl.getServerLastInfo(memberId, channelEventDto.getServerId());
			if (!serverLastInfo.existChannelLastInfo(channelId)) {
				serverLastInfo.addChannelLastInfo(
						ChannelLastInfo.builder()
								.channelId(channelId)
								.lastReadMessageId(channelEventDto.getLastReadMessageId() == null ? 0 : channelEventDto.getLastReadMessageId())
								.lastAccessAt(channelEventDto.getEventTime() == null ? LocalDateTime.now() : channelEventDto.getEventTime())
								.lastReadSequence(channelEventDto.getLastReadSequence() == null ? 0 : channelEventDto.getLastReadSequence())
								.build()
				);
				memberRedisRepositoryImpl.saveServerLastInfo(memberId, channelEventDto.getServerId(), serverLastInfo);
			} else {
				serverLastInfo.updateChannelLastInfo(channelId, channelEventDto.getLastReadMessageId(), channelEventDto.getLastReadSequence(),channelEventDto.getEventTime());
				memberRedisRepositoryImpl.saveServerLastInfo(memberId, channelEventDto.getServerId(), serverLastInfo);
			}
		} else {
			ServerLastInfo serverLastInfo = serverService.getServerLastInfo(memberId, channelEventDto.getServerId());
			memberRedisRepositoryImpl.saveServerLastInfo(memberId, channelEventDto.getServerId(), serverLastInfo);
		}
	}

}
