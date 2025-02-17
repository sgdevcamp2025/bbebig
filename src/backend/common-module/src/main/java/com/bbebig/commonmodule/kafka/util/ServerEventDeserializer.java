package com.bbebig.commonmodule.kafka.util;

import com.bbebig.commonmodule.kafka.dto.serverEvent.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerEventDeserializer implements Deserializer<ServerEventDto> {

	private static final Logger logger = LoggerFactory.getLogger(ServerEventDeserializer.class);
	private final ObjectMapper objectMapper = new ObjectMapper();

	@Override
	public ServerEventDto deserialize(String topic, byte[] data) {
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
			ServerEventType eventType = ServerEventType.fromType(type);

			if (eventType == null) {
				logger.error("유효하지 않은 서버 이벤트 타입: {}", type);
				return null;
			}

			return switch (eventType) {
				case SERVER_MEMBER_PRESENCE -> objectMapper.treeToValue(jsonNode, ServerMemberPresenceEventDto.class);
				case SERVER_MEMBER_ACTION -> objectMapper.treeToValue(jsonNode, ServerMemberActionEventDto.class);
				case SERVER_CHANNEL -> objectMapper.treeToValue(jsonNode, ServerChannelEventDto.class);
				case SERVER_CATEGORY -> objectMapper.treeToValue(jsonNode, ServerCategoryEventDto.class);
				case SERVER_ACTION -> objectMapper.treeToValue(jsonNode, ServerActionEventDto.class);
			};
		} catch (Exception e) {
			logger.error("Kafka 역직렬화 실패 - 토픽: {}, 에러: {}", topic, e.getMessage());
			return null;
		}
	}
}
