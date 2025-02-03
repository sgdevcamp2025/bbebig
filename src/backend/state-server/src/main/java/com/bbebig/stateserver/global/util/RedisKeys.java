package com.bbebig.stateserver.global.util;

public class RedisKeys {

	public static final String STATE_SERVER_KEY_PREFIX = "state:";
	public static final String MEMBER_STATUS_KEY_SUFFIX = ":memberStatus";
	public static final String SERVER_MEMBER_PRESENCE_STATUS_KEY_SUFFIX = ":serverMemberStatus";
	public static final String SERVER_MEMBER_LIST_KEY_SUFFIX = ":memberList";

	public static String getMemberStatusKey(Long memberId) {
		return STATE_SERVER_KEY_PREFIX + memberId + MEMBER_STATUS_KEY_SUFFIX;
	}

	public static String getServerMemberPresenceStatusKey(Long serverId) {
		return STATE_SERVER_KEY_PREFIX + serverId + SERVER_MEMBER_PRESENCE_STATUS_KEY_SUFFIX;
	}

	public static String getServerMemberListKey (Long serverId) {
		return STATE_SERVER_KEY_PREFIX + serverId + SERVER_MEMBER_LIST_KEY_SUFFIX;
	}

}
