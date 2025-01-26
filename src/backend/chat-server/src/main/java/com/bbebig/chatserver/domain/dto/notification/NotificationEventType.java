package com.bbebig.chatserver.domain.dto.notification;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum NotificationEventType {
	FRIEND_PRESENCE("FRIEND-PRESENCE"),
	DM_MEMBER_PRESENCE("DM-MEMBER-PRESENCE"),
	FRIEND_ACTION("FRIEND-UPDATE"),
	DM_MEMBER_ACTION("DM-MEMBER-UPDATE"),
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


