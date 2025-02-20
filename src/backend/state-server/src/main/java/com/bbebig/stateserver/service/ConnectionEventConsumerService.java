package com.bbebig.stateserver.service;

import com.bbebig.commonmodule.clientDto.UserFeignResponseDto.*;
import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import com.bbebig.commonmodule.kafka.dto.ConnectionEventDto;
import com.bbebig.commonmodule.kafka.dto.PresenceEventDto;
import com.bbebig.commonmodule.kafka.dto.model.ConnectionEventType;
import com.bbebig.commonmodule.kafka.dto.model.PresenceType;
import com.bbebig.commonmodule.redis.domain.DeviceInfo;
import com.bbebig.commonmodule.redis.domain.MemberPresenceStatus;
import com.bbebig.stateserver.client.MemberClient;
import com.bbebig.stateserver.dto.DtoConverter;
import com.bbebig.stateserver.repository.MemberRedisRepositoryImpl;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;


@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectionEventConsumerService {

	private final MemberRedisRepositoryImpl memberRedisRepositoryImpl;
	private final KafkaProducerService kafkaProducerService;
	private final MemberClient memberClient;

	@KafkaListener(topics = "${spring.kafka.topic.connection-event}", groupId = "${spring.kafka.consumer.group-id.connection-event}", containerFactory = "connectionEventListener")
	public void consumeForConnectionEvent(ConnectionEventDto connectionEventDto) {
		if (connectionEventDto == null) {
			log.error("[State] ConnectionEventConsumerService: 연결 이벤트 정보 없음");
			return;
		}

		// 개발용 로그
		log.info("[State] ConnectionEventConsumerService: 연결 이벤트 수신. memberId: {}, type: {}", connectionEventDto.getMemberId(), connectionEventDto.getType());

		// 연결 이벤트인지, 연결 끊어짐 이벤트인지 확인
		if (connectionEventDto.getType().equals(ConnectionEventType.CONNECT)) {
			MemberPresenceStatus memberPresenceStatus = handleConnectionEvent(connectionEventDto);

			PresenceEventDto presenceEventDto = DtoConverter.convertMemberPresenceStatusToPresenceEventDto(memberPresenceStatus);

			kafkaProducerService.sendPresenceEvent(presenceEventDto);
		} else if (connectionEventDto.getType().equals(ConnectionEventType.DISCONNECT)) {
			MemberPresenceStatus memberPresenceStatus = handleDisconnectionEvent(connectionEventDto);

			PresenceEventDto presenceEventDto;

			// 연결 끊어짐 이벤트 처리 도중 Redis에 상태 정보가 없는 경우
			if (memberPresenceStatus == null) {
				log.error("[State] ConnectionEventConsumerService: 연결 해제 이벤트 처리 도중 Redis에 상태 정보가 없어서 처리 실패. memberId: {}", connectionEventDto.getMemberId());
				MemberCustomStatusResponseDto responseDto = checkMemberState(connectionEventDto.getMemberId());
				if (responseDto == null) {
					throw new ErrorHandler(ErrorStatus.MEMBER_CUSTOM_STATE_GET_FAILURE);
				}
				presenceEventDto = PresenceEventDto.builder()
						.memberId(connectionEventDto.getMemberId())
						.globalStatus(PresenceType.OFFLINE)
						.actualStatus(PresenceType.OFFLINE)
						.customStatus(responseDto.getCustomStatus())
						.lastActivityTime(LocalDateTime.now())
						.build();
			} else {
				presenceEventDto = DtoConverter.convertMemberPresenceStatusToPresenceEventDto(memberPresenceStatus);
				memberPresenceStatus.deleteDevice(connectionEventDto.getSocketSessionId());
			}
			kafkaProducerService.sendPresenceEvent(presenceEventDto);
		}

	}

	// 연결 이벤트를 처리하여, 상태 정보를 레디스에 저장
	private MemberPresenceStatus handleConnectionEvent(ConnectionEventDto connectionEventDto) {
		MemberPresenceStatus memberPresenceStatus = memberRedisRepositoryImpl.getMemberPresenceStatus(connectionEventDto.getMemberId());
		if (memberPresenceStatus == null) {
			MemberCustomStatusResponseDto responseDto = checkMemberState(connectionEventDto.getMemberId());
			if (responseDto == null) {
				log.error("[State] ConnectionEventConsumerService: 멤버 상태 정보 없음");
				return null;
			}
			memberPresenceStatus = MemberPresenceStatus.builder()
					.memberId(connectionEventDto.getMemberId())
					.actualStatus(PresenceType.ONLINE)
					.globalStatus(responseDto.getCustomStatus())
					.customStatus(responseDto.getCustomStatus())
					.lastActivityTime(LocalDateTime.now())
					.devices(new ArrayList<>())
					.build();
		} else {
			memberPresenceStatus.updateActualStatus(PresenceType.ONLINE);
			memberPresenceStatus.updateLastActivityTime(LocalDateTime.now());
		}

		DeviceInfo deviceInfo = DeviceInfo.builder()
				.platform(connectionEventDto.getPlatform())
				.socketSessionId(connectionEventDto.getSocketSessionId())
				.connectedServerIp(connectionEventDto.getConnectedServerIp())
				.lastActiveTime(LocalDateTime.now())
				.build();

		// 안드로이드 기기인 경우, 로컬에 있던 정보가 있다면 현재 채널 정보를 업데이트
		if (deviceInfo.getPlatform().equals("ANDROID") && connectionEventDto.getCurrentChannelType() != null) {
			deviceInfo.updateCurrent(connectionEventDto.getCurrentChannelType(),
					connectionEventDto.getCurrentChannelId(), connectionEventDto.getCurrentServerId());
		}

		memberPresenceStatus.getDevices().add(deviceInfo);

		memberRedisRepositoryImpl.saveMemberPresenceStatus(connectionEventDto.getMemberId(), memberPresenceStatus);

		return memberPresenceStatus;
	}

	// 연결 끊어짐 이벤트를 확인하여, 상태 정보를 삭제하거나 업데이트
	private MemberPresenceStatus handleDisconnectionEvent(ConnectionEventDto connectionEventDto) {
		MemberPresenceStatus memberPresenceStatus = memberRedisRepositoryImpl.getMemberPresenceStatus(connectionEventDto.getMemberId());

		if (memberPresenceStatus == null) {
			log.error("[State] RedisRepository: 웹소켓 연결 끊어짐 처리시, 멤버 상태 정보 없음");
			return null;
		} else {
			memberPresenceStatus.getDevices().removeIf(deviceInfo -> deviceInfo.getSocketSessionId().equals(connectionEventDto.getSocketSessionId()));

			if (memberPresenceStatus.getDevices().isEmpty()) {
				memberPresenceStatus.setActualStatus(PresenceType.OFFLINE);
			}
			memberPresenceStatus.updateLastActivityTime(LocalDateTime.now());
			memberRedisRepositoryImpl.saveMemberPresenceStatus(connectionEventDto.getMemberId(), memberPresenceStatus);
		}

		memberRedisRepositoryImpl.saveMemberPresenceStatus(connectionEventDto.getMemberId(), memberPresenceStatus);
		return memberPresenceStatus;
	}

	private MemberCustomStatusResponseDto checkMemberState(Long memberId) {
		if (memberId == null || memberId <= 0) {
			log.error("[State] 유효하지 않은 memberId 요청: {}", memberId);
			throw new IllegalArgumentException("유효하지 않은 멤버 ID: " + memberId);
		}
		try {
			MemberCustomStatusResponseDto response = memberClient.getMemberCustomStatus(memberId);
			return response;
		} catch (FeignException e) {
			log.error("[State] FeignException: {}", e.getMessage());
			return null; // 또는 적절한 기본값 반환
		}
	}

}
