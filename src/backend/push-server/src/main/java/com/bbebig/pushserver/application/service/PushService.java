package com.bbebig.pushserver.application.service;

import com.bbebig.commonmodule.kafka.dto.ChatMessageDto;
import com.bbebig.commonmodule.kafka.dto.model.PresenceType;
import com.bbebig.commonmodule.kafka.dto.notification.NotificationEventType;
import com.bbebig.commonmodule.kafka.dto.notification.ServerUnreadEventDto;
import com.bbebig.commonmodule.redis.domain.DeviceInfo;
import com.bbebig.commonmodule.redis.domain.MemberPresenceStatus;
import com.bbebig.commonmodule.redis.domain.ServerMemberStatus;
import com.bbebig.pushserver.application.port.out.MemberRedisPort;
import com.bbebig.pushserver.application.port.out.SendNotificationPort;
import com.bbebig.pushserver.application.port.out.ServerRedisPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class PushService {

	private final SendNotificationPort sendNotificationPort;
	private final MemberRedisPort memberRedisPort;
	private final ServerRedisPort serverRedisPort;

	public void sendUnreadPush(ChatMessageDto chatMessageDto) {
		List<ServerMemberStatus> allServerMemberStatus = serverRedisPort.getAllServerMemberStatus(chatMessageDto.getServerId());

		for (ServerMemberStatus serverMemberStatus : allServerMemberStatus) {
			if (serverMemberStatus.getMemberId().equals(chatMessageDto.getSendMemberId())) {
				continue;
			}
			if (serverMemberStatus.getActualStatus() == PresenceType.ONLINE) {
				MemberPresenceStatus memberPresenceStatus = memberRedisPort.getMemberPresenceStatus(serverMemberStatus.getMemberId());
				List<DeviceInfo> devices = memberPresenceStatus.getDevices();

				boolean isLooking = false;

				for (DeviceInfo device : devices) {
					if (Objects.equals(device.getCurrentServerId(), chatMessageDto.getServerId()) &&
						Objects.equals(device.getCurrentChannelId(), chatMessageDto.getChannelId())) {
						isLooking = true;
						break;
					}
				}
				if (!isLooking) {
					for (DeviceInfo device : devices) {
						if (!Objects.equals(device.getCurrentServerId(), chatMessageDto.getServerId())) {
							ServerUnreadEventDto serverUnreadEventDto = ServerUnreadEventDto.builder()
								.memberId(memberPresenceStatus.getMemberId())
								.type(NotificationEventType.SERVER_UNREAD)
								.serverId(chatMessageDto.getServerId())
								.channelId(chatMessageDto.getChannelId())
								.sequence(chatMessageDto.getSequence())
								.targetSessionId(device.getSocketSessionId())
								.status("UNREAD")
								.build();
							sendNotificationPort.sendNotification(serverUnreadEventDto);
						}
					}
				}


			}

		}


	}
}

