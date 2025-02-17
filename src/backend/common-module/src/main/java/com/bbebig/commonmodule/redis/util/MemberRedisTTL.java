package com.bbebig.commonmodule.redis.util;

public class MemberRedisTTL {

	public static final int SERVER_LAST_INFO_TTL = 7;

	public int getChannelLastInfoTTL() {
		return SERVER_LAST_INFO_TTL;
	}

}
