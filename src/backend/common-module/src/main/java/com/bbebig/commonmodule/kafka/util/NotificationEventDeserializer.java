package com.bbebig.commonmodule.kafka.util;

import com.bbebig.commonmodule.kafka.dto.notification.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotificationEventDeserializer implements Deserializer<NotificationEventDto> {

	private static final Logger logger = LoggerFactory.getLogger(NotificationEventDeserializer.class);
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public NotificationEventDto deserialize(String topic, byte[] data) {
		try {
			if (data == null || data.length == 0) {
				logger.warn("Kafka 메시지가 비어 있습니다. 토픽: {}", topic);
				return null;
			}

			JsonNode jsonNode = objectMapper.readTree(data);

			// JSON 내 `type` 필드 체크 후 처리
			if (!jsonNode.has("type")) {
				logger.error("Kafka 메시지에 'type' 필드가 없습니다. 토픽: {}", topic);
				return null;
			}

			String type = jsonNode.get("type").asText();
			NotificationEventType eventType = NotificationEventType.fromType(type);

			if (eventType == null) {
				logger.error("유효하지 않은 알림 이벤트 타입: {} (토픽: {})", type, topic);
				return null;
			}

			return switch (eventType) {
				case DM_ACTION -> objectMapper.treeToValue(jsonNode, DmActionEventDto.class);
				case FRIEND_PRESENCE -> objectMapper.treeToValue(jsonNode, FriendPresenceEventDto.class);
				case DM_MEMBER_PRESENCE -> objectMapper.treeToValue(jsonNode, DmMemberPresenceEventDto.class);
				case FRIEND_ACTION -> objectMapper.treeToValue(jsonNode, FriendActionEventDto.class);
				case DM_MEMBER_ACTION -> objectMapper.treeToValue(jsonNode, DmMemberActionEventDto.class);
				case SERVER_UNREAD -> objectMapper.treeToValue(jsonNode, ServerUnreadEventDto.class);
				case FRIEND_REQUEST -> objectMapper.treeToValue(jsonNode, FriendRequestEventDto.class);
			};
		} catch (Exception e) {
			logger.error("Kafka 역직렬화 실패 - 토픽: {}, 에러: {}", topic, e.getMessage(), e);
			return null;
		}
	}
}
