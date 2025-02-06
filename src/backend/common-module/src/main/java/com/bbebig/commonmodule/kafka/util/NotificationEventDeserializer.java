package com.bbebig.commonmodule.kafka.util;

import com.bbebig.commonmodule.global.response.code.error.ErrorStatus;
import com.bbebig.commonmodule.global.response.exception.ErrorHandler;
import com.bbebig.commonmodule.kafka.dto.notification.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

public class NotificationEventDeserializer implements Deserializer<NotificationEventDto> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public NotificationEventDto deserialize(String topic, byte[] data) {
		try {
			JsonNode jsonNode = objectMapper.readTree(data);

			// JSON 내 `type` 필드를 기반으로 `ServerEventType` 결정
			String type = jsonNode.get("type").asText();
			NotificationEventType eventType = NotificationEventType.fromType(type);

			if (eventType == null) {
				throw new ErrorHandler(ErrorStatus.INVALID_NOTIFICATION_EVENT_TYPE);
			}

			return switch (eventType) {
				case DM_ACTION -> objectMapper.treeToValue(jsonNode, DmActionEventDto.class);
				case FRIEND_PRESENCE -> objectMapper.treeToValue(jsonNode, FriendPresenceEventDto.class);
				case DM_MEMBER_PRESENCE -> objectMapper.treeToValue(jsonNode, DmMemberPresenceEventDto.class);
				case FRIEND_ACTION -> objectMapper.treeToValue(jsonNode, FriendActionEventDto.class);
				case DM_MEMBER_ACTION -> objectMapper.treeToValue(jsonNode, DmMemberActionEventDto.class);
			};
		} catch (Exception e) {
			throw new SerializationException("Error deserializing message", e);
		}
	}
}
