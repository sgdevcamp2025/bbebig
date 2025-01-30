package com.bbebig.stateserver.repository;

import com.bbebig.commonmodule.kafka.dto.ConnectionEventDto;
import com.bbebig.stateserver.client.MemberClient;
import com.bbebig.stateserver.domain.DeviceInfo;
import com.bbebig.stateserver.domain.MemberPresenceStatus;
import com.bbebig.stateserver.dto.MemberResponseDto.MemberGlobalStatusResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisRepository {

	private static final String MEMBER_KEY_PREFIX = "member:";
	private static final String MEMBER_STATUS_KEY_SUFFIX = ":memberStatus";

	private final RedisTemplate<String, Object> redisTemplate;

	private final MemberClient memberClient;


	// TODO : 반환 타입 잘 고민해보기

	public void handleConnection(ConnectionEventDto connectionEventDto) {
		String key = MEMBER_KEY_PREFIX + connectionEventDto.getMemberId() + MEMBER_STATUS_KEY_SUFFIX;

		MemberPresenceStatus status = loadMemberPresenceStatus(key);
		if (status == null) {
			MemberGlobalStatusResponseDto responseDto = memberClient.getMemberGlobalStatus(connectionEventDto.getMemberId());
			status = MemberPresenceStatus.builder()
					.globalStatus(responseDto.getGlobalStatus())
					.actualStatus("ONLINE")
					.lastActivityTime(LocalDateTime.now().toString())
					.devices(new ArrayList<>())
					.build();
		}

		DeviceInfo deviceInfo = DeviceInfo.builder()
				.platform(connectionEventDto.getPlatform())
				.socketSessionId(connectionEventDto.getSocketSessionId())
				.connectedServerIp(connectionEventDto.getConnectedServerIp())
				.lastActiveTime(LocalDateTime.now().toString())
				.build();
		if (deviceInfo.getPlatform().equals("ANDROID")) {
			deviceInfo.updateCurrent(connectionEventDto.getCurrentRoomType(),
					connectionEventDto.getCurrentChannelId(), connectionEventDto.getCurrentServerId());
		}

		status.getDevices().add(deviceInfo);

		saveMemberPresenceStatus(key, status);
	}

	private MemberPresenceStatus loadMemberPresenceStatus(String key) {
		Object obj = redisTemplate.opsForValue().get(key);
		if (obj instanceof MemberPresenceStatus) {
			return (MemberPresenceStatus) obj;
		}
		return null;
	}

	private void saveMemberPresenceStatus(String key, MemberPresenceStatus status) {
		redisTemplate.opsForValue().set(key, status);
	}

}
