package com.bbebig.serviceserver.global.kafka;


import com.bbebig.commonmodule.kafka.dto.MemberEventDto;
import com.bbebig.commonmodule.kafka.dto.model.MemberEventType;
import com.bbebig.commonmodule.kafka.dto.serverEvent.ServerEventType;
import com.bbebig.commonmodule.kafka.dto.serverEvent.ServerMemberActionEventDto;
import com.bbebig.commonmodule.kafka.dto.serverEvent.status.ServerMemberActionStatus;
import com.bbebig.serviceserver.server.service.ServerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberEventConsumerService {

	private final ServerService serverService;
	private final KafkaProducerService kafkaProducerService;

	@KafkaListener(topics = "${spring.kafka.topic.member-event}", groupId = "${spring.kafka.consumer.group-id.member-event}", containerFactory = "memberEventListener")
	public void consumeForMemberEvent(MemberEventDto memberEventDto) {
		if (memberEventDto == null) {
			log.error("[State] MemberEventConsumerService: 멤버 이벤트 정보 없음");
			return;
		}
		// 개발용 로그
		log.info("[State] MemberEventConsumerService: 멤버 이벤트 수신. dto: {}", memberEventDto);
		if (memberEventDto.getType().equals(MemberEventType.MEMBER_UPDATE)) {
			handleUpdateEvent(memberEventDto);
		} else if (memberEventDto.getType().equals(MemberEventType.MEMBER_DELETE)) {
			handleDeleteEvent(memberEventDto);
		} else {
			if (!memberEventDto.getType().equals(MemberEventType.MEMBER_CREATE)) {
				log.error("[State] MemberEventConsumerService: 멤버 이벤트 타입이 잘못되었습니다. memberEventDto: {}", memberEventDto);
			}
		}
	}

	private void handleUpdateEvent(MemberEventDto memberEventDto) {
		Long memberId = memberEventDto.getMemberId();
		serverService.getMemberServerList(memberId).getServerIdList().forEach(serverId -> {
			ServerMemberActionEventDto actionEventDto = ServerMemberActionEventDto.builder()
					.serverId(serverId)
					.type(ServerEventType.SERVER_MEMBER_ACTION)
					.memberId(memberId)
					.nickname(memberEventDto.getNickname())
					.avatarUrl(memberEventDto.getAvatarUrl())
					.bannerUrl(memberEventDto.getBannerUrl())
					.status(ServerMemberActionStatus.UPDATE)
					.build();
			kafkaProducerService.sendServerEvent(actionEventDto);
		});
	}

	private void handleDeleteEvent(MemberEventDto memberEventDto) {
		Long memberId = memberEventDto.getMemberId();
		serverService.getMemberServerList(memberId).getServerIdList().forEach(serverId -> {
			ServerMemberActionEventDto actionEventDto = ServerMemberActionEventDto.builder()
					.serverId(serverId)
					.type(ServerEventType.SERVER_MEMBER_ACTION)
					.memberId(memberId)
					.nickname(memberEventDto.getNickname())
					.avatarUrl(memberEventDto.getAvatarUrl())
					.bannerUrl(memberEventDto.getBannerUrl())
					.status(ServerMemberActionStatus.LEAVE)
					.build();
			kafkaProducerService.sendServerEvent(actionEventDto);

			// 서버에서 탈퇴 처리
			serverService.withdrawServer(memberId, serverId);
		});

	}
}
