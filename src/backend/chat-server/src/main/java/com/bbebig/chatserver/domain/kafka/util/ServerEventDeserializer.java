package com.bbebig.chatserver.domain.kafka.util;

import com.bbebig.chatserver.domain.kafka.dto.serverEvent.*;
import com.bbebig.chatserver.global.response.code.error.ErrorStatus;
import com.bbebig.chatserver.global.response.exception.ErrorHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Deserializer;

public class ServerEventDeserializer implements Deserializer<ServerEventDto> {

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public ServerEventDto deserialize(String topic, byte[] data) {
		try {
			JsonNode jsonNode = objectMapper.readTree(data);

			// JSON 내 `type` 필드를 기반으로 `ServerEventType` 결정
			String type = jsonNode.get("type").asText();
			ServerEventType eventType = ServerEventType.fromType(type);

			if (eventType == null) {
				throw new ErrorHandler(ErrorStatus.INVALID_SERVER_EVENT_TYPE);
			}

			return switch (eventType) {
				case SERVER_MEMBER_PRESENCE -> objectMapper.treeToValue(jsonNode, ServerMemberPresenceEventDto.class);
				case SERVER_MEMBER_ACTION -> objectMapper.treeToValue(jsonNode, ServerMemberActionEventDto.class);
				case SERVER_CHANNEL -> objectMapper.treeToValue(jsonNode, ServerChannelEventDto.class);
				case SERVER_CATEGORY -> objectMapper.treeToValue(jsonNode, ServerCategoryEventDto.class);
			};
		} catch (Exception e) {
			throw new SerializationException("Error deserializing message", e);
		}
	}
}
