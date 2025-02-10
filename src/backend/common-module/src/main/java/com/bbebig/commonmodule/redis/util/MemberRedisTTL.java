package com.bbebig.commonmodule.redis.util;

public class MemberRedisTTL {

	public static final int RECENT_CHANNEL_INFO_TTL_DATE = 3; // 3일

	public static int getRecentChannelInfoTTLDate() {
		return RECENT_CHANNEL_INFO_TTL_DATE;
	}

}
