package com.bbebig.commonmodule.redis.util;

public class RedisTTL {
	// 1일 = 60초 * 60분 * 24시간
	private static final int DEFAULT_ONE_DAY_SECOND = 60 * 60 * 24; // 1일 (86400초)

	// 개인 DM 멤버 리스트의 TTL
	private static final int PRIVATE_DM_MEMBER_LIST_TTL_DATE = 1; // 1일

	// 그룹 DM 멤버 리스트의 TTL
	private static final int GROUP_DM_MEMBER_LIST_TTL_DATE = 3; // 3일

	public static int getPrivateDmMemberListTTLDate() {
		return DEFAULT_ONE_DAY_SECOND * PRIVATE_DM_MEMBER_LIST_TTL_DATE;
	}

	public static int getGroupDmMemberListTTLDate() {
		return DEFAULT_ONE_DAY_SECOND * GROUP_DM_MEMBER_LIST_TTL_DATE;
	}
}
