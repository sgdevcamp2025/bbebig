package com.bbebig.signalingserver.domain;

public class SignalRedisKeys {

	public static final String SIGNAL_KEY_PREFIX = "signal:";

	public static final String CHANNEL_MEMBER_LIST_KEY_SUFFIX = ":channelMemberList";

	public static String getChannelMemberListKey(Long channelId) {
		return SIGNAL_KEY_PREFIX + channelId + CHANNEL_MEMBER_LIST_KEY_SUFFIX;
	}
}
