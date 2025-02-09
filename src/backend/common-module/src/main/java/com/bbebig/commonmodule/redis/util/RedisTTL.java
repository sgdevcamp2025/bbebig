package com.bbebig.commonmodule.redis.util;

public class RedisTTL {
	// 개인 DM 멤버 리스트의 TTL
	private static final int PRIVATE_DM_MEMBER_LIST_TTL_DATE = 1; // 1일

	// 그룹 DM 멤버 리스트의 TTL
	private static final int GROUP_DM_MEMBER_LIST_TTL_DATE = 3; // 3일

	// 최근 디엠 및 서버 채널의 접속 정보 TTL
	private static final int RECENT_CHANNEL_INFO_TTL_DATE = 3; // 3일

	public static int getPrivateDmMemberListTTLDate() {
		return PRIVATE_DM_MEMBER_LIST_TTL_DATE;
	}

	public static int getGroupDmMemberListTTLDate() {
		return GROUP_DM_MEMBER_LIST_TTL_DATE;
	}

	public static int getRecentChannelInfoTTLDate() {
		return RECENT_CHANNEL_INFO_TTL_DATE;
	}
}
