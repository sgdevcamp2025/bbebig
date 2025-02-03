package com.bbebig.stateserver.global.util;

public class RedisKeys {

	public static final String STATE_SERVER_KEY_PREFIX = "state:";
	public static final String MEMBER_STATUS_KEY_SUFFIX = ":memberStatus";

	public static String getMemberStatusKey(Long memberId) {
		return STATE_SERVER_KEY_PREFIX + memberId + MEMBER_STATUS_KEY_SUFFIX;
	}

}
