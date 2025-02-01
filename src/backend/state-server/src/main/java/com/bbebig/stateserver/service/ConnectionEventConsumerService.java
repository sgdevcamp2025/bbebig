package com.bbebig.stateserver.service;

import com.bbebig.commonmodule.kafka.dto.ConnectionEventDto;
import com.bbebig.commonmodule.kafka.dto.PresenceEventDto;
import com.bbebig.commonmodule.kafka.dto.model.PresenceType;
import com.bbebig.stateserver.client.MemberClient;
import com.bbebig.stateserver.domain.DeviceInfo;
import com.bbebig.stateserver.domain.MemberPresenceStatus;
import com.bbebig.stateserver.dto.DtoConverter;
import com.bbebig.stateserver.global.util.RedisKeys;
import com.bbebig.stateserver.repository.RedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static com.bbebig.stateserver.dto.MemberResponseDto.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ConnectionEventConsumerService {

	private final RedisRepository redisRepository;
	private final KafkaProducerService kafkaProducerService;
	private final MemberClient memberClient;

	@KafkaListener(topics = "${spring.kafka.topic.connection-event}", groupId = "${spring.kafka.consumer.group-id.connection-event}", containerFactory = "connectionEventListener")
	public void consumeForChannelChatEvent(ConnectionEventDto connectionEventDto) {
		if (connectionEventDto == null) {
			log.error("[State] ConnectionEventConsumerService: 연결 이벤트 정보 없음");
			return;
		}
		// 연결 이벤트인지, 연결 끊어짐 이벤트인지 확인
		if (connectionEventDto.getType().equals("CONNECT")) {
			MemberPresenceStatus memberPresenceStatus = handleConnectionEvent(connectionEventDto);

			PresenceEventDto presenceEventDto = DtoConverter.convertMemberPresenceStatusToPresenceEventDto(memberPresenceStatus);

			kafkaProducerService.sendPresenceEvent(presenceEventDto);
		} else if (connectionEventDto.getType().equals("DISCONNECT")) {
			MemberPresenceStatus memberPresenceStatus = handleDisconnectionEvent(connectionEventDto);

			PresenceEventDto presenceEventDto;

			// 연결 끊어짐 이벤트 처리 도중 Redis에 상태 정보가 없는 경우
			if (memberPresenceStatus == null) {
				log.error("[State] ConnectionEventConsumerService: 연결 해제 이벤트 처리 도중 Redis에 상태 정보가 없어서 처리 실패. memberId: {}", connectionEventDto.getMemberId());
				presenceEventDto = PresenceEventDto.builder()
						.memberId(connectionEventDto.getMemberId())
						.globalStatus(PresenceType.OFFLINE)
						.actualStatus(PresenceType.OFFLINE)
						.lastActivityTime(LocalDateTime.now())
						.build();
			} else {
				presenceEventDto = DtoConverter.convertMemberPresenceStatusToPresenceEventDto(memberPresenceStatus);
			}
			kafkaProducerService.sendPresenceEvent(presenceEventDto);
		}

	}

	// 연결 이벤트를 처리하여, 상태 정보를 레디스에 저장
	private MemberPresenceStatus handleConnectionEvent(ConnectionEventDto connectionEventDto) {
		String key = RedisKeys.getMemberStatusKey(connectionEventDto.getMemberId());

		MemberPresenceStatus status = redisRepository.loadMemberPresenceStatus(key);
		if (status == null) {
			MemberGlobalStatusResponseDto memberGlobalStatus = memberClient.getMemberGlobalStatus(connectionEventDto.getMemberId());
			status = MemberPresenceStatus.builder()
					.memberId(connectionEventDto.getMemberId())
					.globalStatus(memberGlobalStatus.getGlobalStatus())
					.actualStatus(PresenceType.ONLINE)
					.lastActivityTime(LocalDateTime.now())
					.devices(new ArrayList<>())
					.build();
		} else {
			status.setActualStatus(PresenceType.ONLINE);
			status.updateLastActivityTime(LocalDateTime.now());
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

		status.getDevices().add(deviceInfo);

		redisRepository.saveMemberPresenceStatus(key, status);
		return status;
	}

	// 연결 끊어짐 이벤트를 확인하여, 상태 정보를 삭제하거나 업데이트
	private MemberPresenceStatus handleDisconnectionEvent(ConnectionEventDto connectionEventDto) {
		String key = RedisKeys.getMemberStatusKey(connectionEventDto.getMemberId());
		MemberPresenceStatus status = redisRepository.loadMemberPresenceStatus(key);

		if (status == null) {
			log.error("[State] RedisRepository: 웹소켓 연결 끊어짐 처리시, 멤버 상태 정보 없음");
			return null;
		} else {
			status.getDevices().removeIf(deviceInfo -> deviceInfo.getSocketSessionId().equals(connectionEventDto.getSocketSessionId()));

			if (status.getDevices().isEmpty()) {
				status.setActualStatus(PresenceType.OFFLINE);
			}
			status.updateLastActivityTime(LocalDateTime.now());
			redisRepository.saveMemberPresenceStatus(key, status);
		}

		redisRepository.saveMemberPresenceStatus(key, status);
		return status;
	}

}
