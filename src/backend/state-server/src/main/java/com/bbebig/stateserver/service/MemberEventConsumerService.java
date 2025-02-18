package com.bbebig.stateserver.service;

import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import com.bbebig.commonmodule.kafka.dto.MemberEventDto;
import com.bbebig.stateserver.repository.MemberRedisRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberEventConsumerService {

	private final StateService stateService;
	private final MemberRedisRepositoryImpl memberRedisRepositoryImpl;

	@KafkaListener(topics = "${spring.kafka.topic.member-event}", groupId = "${spring.kafka.consumer.group-id.member-event}", containerFactory = "memberEventListener")
	public void consumeForMemberEvent(MemberEventDto memberEventDto) {
		if (memberEventDto == null) {
			log.error("[State] MemberEventConsumerService: 멤버 이벤트 정보 없음");
			return;
		}

		// 개발용 로그
		log.info("[State] MemberEventConsumerService: 멤버 이벤트 수신. memberId: {}, type: {}", memberEventDto.getMemberId(), memberEventDto.getType());

		if (memberEventDto.getType().equals("DELETE")) {
			memberRedisRepositoryImpl.deleteMemberPresenceStatus(memberEventDto.getMemberId());
		} else if (memberEventDto.getType().equals("PRESENCE_UPDATE")) {
			stateService.updateMemberPresenceStatus(memberEventDto.getMemberId(), memberEventDto.getGlobalStatus());
		}
		else {
			if (!memberEventDto.getType().equals("CREATE")) {
				throw new ErrorHandler(ErrorStatus.INVALID_SERVER_EVENT_TYPE);
			}
			return;
		}
	}

}
