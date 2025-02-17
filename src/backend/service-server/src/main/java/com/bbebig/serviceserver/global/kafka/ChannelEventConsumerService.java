package com.bbebig.serviceserver.global.kafka;


import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import com.bbebig.commonmodule.kafka.dto.ChannelEventDto;
import com.bbebig.commonmodule.redis.domain.ChannelLastInfo;
import com.bbebig.commonmodule.redis.domain.ServerLastInfo;
import com.bbebig.serviceserver.channel.entity.ChannelMember;
import com.bbebig.serviceserver.channel.repository.ChannelMemberRepository;
import com.bbebig.serviceserver.channel.service.ChannelService;
import com.bbebig.serviceserver.server.repository.MemberRedisRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelEventConsumerService {

	private final ChannelMemberRepository channelMemberRepository;
	private final MemberRedisRepositoryImpl memberRedisRepositoryImpl;

	private final ChannelService channelService;

	@KafkaListener(topics = "${spring.kafka.topic.channel-event}", groupId = "${spring.kafka.consumer.group-id.channel-event}", containerFactory = "channelEventListener")
	public void consumeForChannelEvent(ChannelEventDto channelEventDto) {
		if (channelEventDto == null) {
			log.error("[State] ChannelEventConsumerService: 채널 이벤트 정보 없음");
			return;
		}

		// 개발용 로그
		log.info("[State] ChannelEventConsumerService: 채널 이벤트 수신. memberId: {}, type: {}, dto: {}", channelEventDto.getMemberId(), channelEventDto.getType(), channelEventDto);
		if (channelEventDto.getType().equals("ENTER") || channelEventDto.getType().equals("LEAVE")) {
			handleChannelEvent(channelEventDto);
		} else {
			log.error("[State] ChannelEventConsumerService: 채널 이벤트 타입이 잘못되었습니다. channelEventDto: {}", channelEventDto);
		}
	}

	private void handleChannelEvent(ChannelEventDto channelEventDto) {
		// TODO : 예외 처리 어떻게 할지. ex) 레디스 캐싱정보 토대로 확인 등
		Long memberId = channelEventDto.getMemberId();
		Long channelId = channelEventDto.getChannelId();

		ChannelMember channelMember = channelMemberRepository.findByServerMemberIdAndChannelId(memberId, channelId)
				.orElseThrow(() -> new ErrorHandler(ErrorStatus.CHANNEL_MEMBER_NOT_FOUND));

		channelMember.updateLastInfo(channelEventDto.getLastReadMessageId(), channelEventDto.getEventTime());
		channelMemberRepository.save(channelMember);

		if (memberRedisRepositoryImpl.existsServerLastInfo(memberId, channelEventDto.getServerId())) {
			ServerLastInfo serverChannelLastInfo = memberRedisRepositoryImpl.getServerLastInfo(memberId, channelEventDto.getServerId());
			if (!serverChannelLastInfo.existChannelLastInfo(channelId)) {
				serverChannelLastInfo.addChannelLastInfo(
						ChannelLastInfo.builder()
								.channelId(channelId)
								.lastReadMessageId(channelEventDto.getLastReadMessageId())
								.lastAccessAt(channelEventDto.getEventTime())
								.build()
				);
				log.error("[State] ChannelEventConsumerService: 채널 정보가 없어서 업데이트 실패. memberId: {}, channelId: {}", memberId, channelId);
			} else {
				serverChannelLastInfo.updateChannelLastInfo(channelId, channelEventDto.getLastReadMessageId(), channelEventDto.getEventTime());
				memberRedisRepositoryImpl.saveServerLastInfo(memberId, channelEventDto.getServerId(), serverChannelLastInfo);
			}
		} else {
			channelService.getChannelLastInfo(memberId, channelEventDto.getServerId());
		}
	}

}
