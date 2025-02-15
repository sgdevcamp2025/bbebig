package com.bbebig.userserver.global.kafka;

import com.bbebig.commonmodule.clientDto.serviceServer.CommonServiceServerClientResponseDto;
import com.bbebig.commonmodule.kafka.dto.PresenceEventDto;
import com.bbebig.commonmodule.kafka.dto.notification.FriendPresenceEventDto;
import com.bbebig.userserver.friend.entity.Friend;
import com.bbebig.userserver.friend.entity.FriendStatus;
import com.bbebig.userserver.friend.repository.FriendRepository;
import com.bbebig.userserver.friend.service.FriendService;
import com.bbebig.userserver.member.service.KafkaProducerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PresenceEventConsumerService {

	private final KafkaProducerService kafkaProducerService;
	private final FriendService friendService;

	@KafkaListener(topics = "${spring.kafka.topic.presence-event}", groupId = "${spring.kafka.consumer.group-id.presence-event}", containerFactory = "presenceEventListener")
	public void consumeForPresenceEvent(PresenceEventDto presenceEventDto) {
		if (presenceEventDto == null) {
			log.error("[State] PresenceEventConsumerService: 프레즌스 이벤트 정보 없음");
			return;
		}

		handleEvent(presenceEventDto);
	}

	private void handleEvent(PresenceEventDto presenceEventDto) {
		// 개발용 로그
		log.info("[State] PresenceEventConsumerService: 온라인 이벤트 처리. memberId: {}, globalStatus: {}, actualStatus: {}, lastActivityTime: {}", presenceEventDto.getMemberId(), presenceEventDto.getGlobalStatus(), presenceEventDto.getActualStatus(), presenceEventDto.getLastActivityTime());
		Long memberId = presenceEventDto.getMemberId();
		List<Long> friendMemberIds = friendService.getMemberFriendIdList(memberId);
		for (Long friendMemberId : friendMemberIds) {
			FriendPresenceEventDto friendPresenceEventDto = FriendPresenceEventDto.builder()
					.memberId(friendMemberId)
					.friendId(memberId)
					.globalStatus(presenceEventDto.getGlobalStatus())
					.build();
			kafkaProducerService.sendNotificationEvent(friendPresenceEventDto);
		}

	}
}
