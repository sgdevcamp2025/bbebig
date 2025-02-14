package com.bbebig.stateserver.service;

import com.bbebig.commonmodule.kafka.dto.notification.DmActionEventDto;
import com.bbebig.commonmodule.kafka.dto.notification.DmMemberActionEventDto;
import com.bbebig.commonmodule.kafka.dto.notification.NotificationEventDto;
import com.bbebig.commonmodule.kafka.dto.notification.NotificationEventType;
import com.bbebig.commonmodule.kafka.dto.serverEvent.ServerActionEventDto;
import com.bbebig.stateserver.repository.DmRedisRepositoryImpl;
import com.bbebig.stateserver.repository.MemberRedisRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationEventConsumerService {

	private final StateService stateService;
	private final MemberRedisRepositoryImpl memberRedisRepositoryImpl;
	private final DmRedisRepositoryImpl dmRedisRepositoryImpl;

	@KafkaListener(topics = "${spring.kafka.topic.notification-event}", groupId = "${spring.kafka.consumer.group-id.notification-event}", containerFactory = "notificationEventListener")
	public void consumeForNotificationEvent(NotificationEventDto eventDto) {
		if (eventDto == null) {
			log.error("[State] NotificationEventConsumerService: 알림 이벤트 정보 없음");
			return;
		}

		if (eventDto.getType().equals(NotificationEventType.DM_MEMBER_ACTION)) {
			DmMemberActionEventDto dmMemberActionEventDto = (DmMemberActionEventDto) eventDto;
			handleDmMemberActionEvent(dmMemberActionEventDto);
		} else if (eventDto.getType().equals(NotificationEventType.DM_ACTION)) {
			DmActionEventDto dmActionEventDto = (DmActionEventDto) eventDto;
			handleDmActionEvent(dmActionEventDto);
		} else {
			if (!(eventDto.getType().equals(NotificationEventType.DM_MEMBER_PRESENCE) ||
					eventDto.getType().equals(NotificationEventType.FRIEND_ACTION) ||
					eventDto.getType().equals(NotificationEventType.FRIEND_PRESENCE))
			) {
				log.error("[State] NotificationEventConsumerService: 알림 이벤트 타입이 잘못되었습니다. eventDto: {}", eventDto);
			}

		}

	}

	private void handleDmMemberActionEvent(DmMemberActionEventDto dmMemberActionEventDto) {
		Long channelId = dmMemberActionEventDto.getChannelId();

		// TODO : 추후 DM이 개발 완료되면 이어서 개발

//		if (!dmRedisRepositoryImpl.existsDmMemberList(channelId)) {
//			stateService.makeDmMemberList(channelId);
//		}

//		if (dmMemberActionEventDto.getStatus().equals("JOIN")) {
//			dmRedisRepositoryImpl.addDmMemberToSet(channelId, dmMemberActionEventDto.getMemberId());
//		} else if (dmMemberActionEventDto.getStatus().equals("LEAVE")) {
//			dmRedisRepositoryImpl.removeDmMemberFromSet(channelId, dmMemberActionEventDto.getMemberId());
//		} else {
//			if (!dmMemberActionEventDto.getStatus().equals("UPDATE")) {
//				log.error("[State] NotificationEventConsumerService: DM 멤버 액션 이벤트 타입이 잘못되었습니다. dmMemberActionEventDto: {}", dmMemberActionEventDto);
//			}
//		}
	}

	private void handleDmActionEvent(DmActionEventDto dmActionEventDto) {
		Long channelId = dmActionEventDto.getChannelId();

		// TODO : 추후 DM이 개발 완료되면 이어서 개발

//		if (dmActionEventDto.getStatus().equals("CREATE")) {
//			stateService.makeDmMemberList(channelId);
//		} else if (dmActionEventDto.getStatus().equals("DELETE")) {
//			dmRedisRepositoryImpl.deleteDmMemberList(channelId);
//		} else {
//			if (!dmActionEventDto.getStatus().equals("UPDATE")) {
//				log.error("[State] NotificationEventConsumerService: DM 액션 이벤트 타입이 잘못되었습니다. dmActionEventDto: {}", dmActionEventDto);
//			}
//
//		}
	}
}
