package com.bbebig.commonmodule.kafka.dto.serverEvent;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum ServerEventType {
	SERVER_MEMBER_PRESENCE("SERVER_MEMBER_PRESENCE"),
	SERVER_MEMBER_ACTION("SERVER_MEMBER_ACTION"),
	SERVER_CHANNEL("SERVER_CHANNEL"),
	SERVER_CATEGORY("SERVER_CATEGORY");

	private final String type;

	private static final Map<String, ServerEventType> TYPE_MAP = new HashMap<>();

	static {
		for (ServerEventType eventType : values()) {
			TYPE_MAP.put(eventType.getType(), eventType);
		}
	}

	ServerEventType(String type) {
		this.type = type;
	}

	public static ServerEventType fromType(String type) {
		return TYPE_MAP.getOrDefault(type, null);
	}
}

