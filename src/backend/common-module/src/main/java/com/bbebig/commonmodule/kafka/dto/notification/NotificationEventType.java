package com.bbebig.commonmodule.kafka.dto.notification;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum NotificationEventType {
	DM_ACTION("DM_ACTION"),
	FRIEND_PRESENCE("FRIEND_PRESENCE"),
	DM_MEMBER_PRESENCE("DM_MEMBER_PRESENCE"),
	FRIEND_ACTION("FRIEND_ACTION"),
	DM_MEMBER_ACTION("DM_MEMBER_UPDATE"),
	SERVER_UNREAD("SERVER_UNREAD"),
	;

	private final String type;

	private static final Map<String, NotificationEventType> TYPE_MAP = new HashMap<>();

	static {
		for (NotificationEventType eventType : values()) {
			TYPE_MAP.put(eventType.getType(), eventType);
		}
	}

	NotificationEventType(String type) {
		this.type = type;
	}

	public static NotificationEventType fromType(String type) {
		return TYPE_MAP.getOrDefault(type, null);
	}
}


