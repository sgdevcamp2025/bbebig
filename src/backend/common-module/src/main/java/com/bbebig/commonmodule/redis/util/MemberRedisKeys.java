package com.bbebig.commonmodule.redis.util;

/**
 * Redis 키 생성 유틸리티.
 * 각 키의 패턴 / 용도 / 자료구조 / TTL / 사용 서비스 등을 정의.
 */
public class MemberRedisKeys {

	// Prefix
	private static final String MEMBER_KEY_PREFIX = "member:";

	// Suffix
	private static final String MEMBER_STATUS_KEY_SUFFIX = ":memberStatus";
	private static final String MEMBER_DM_LIST_KEY_SUFFIX = ":dmList";
	private static final String MEMBER_SERVER_LIST_KEY_SUFFIX = ":serverList";
	private static final String MEMBER_FRIEND_LIST_KEY_SUFFIX = ":friendList";

	private static final String SERVER_LAST_INFO_KEY_SUFFIX = ":serverLastInfo";


	/**
	 * Key pattern: "member:{memberId}:memberStatus"
	 * Type: String
	 * Value: JSON (예: MemberStatus)
	 * TTL: none (만료 없이 유지. 추후에 설정)
	 * Used by: State Server, Service Server, Push Server
	 *
	 * 예시 Key: member:100:memberStatus
	 * 해당 String은 멤버의 상태를 JSON 형태로 저장.
	 *
	 * @param memberId 멤버 ID
	 * @return "member:{memberId}:memberStatus"
	 */
	public static String getMemberStatusKey(Long memberId) {
		return MEMBER_KEY_PREFIX + memberId + MEMBER_STATUS_KEY_SUFFIX;
	}

	/**
	 * Key pattern: "member:{memberId}:serverList"
	 * Type: Set
	 * Value: serverId (Long/String)들의 집합
	 * TTL: none (필요 시 추후 설정)
	 * Used by: State Server, Service Server, Push Server 등
	 *
	 * 예시 Key: member:100:serverList
	 * 해당 Set은 "멤버가 속한 서버(길드) 아이디" 전체를 저장.
	 * ex) SADD member:100:serverList 1,2,3
	 *
	 * @param memberId 멤버 ID
	 * @return "member:{memberId}:serverList"
	 */
	public static String getMemberServerListKey(Long memberId) {
		return MEMBER_KEY_PREFIX + memberId + MEMBER_SERVER_LIST_KEY_SUFFIX;
	}

	/**
	 * Key pattern: "member:{memberId}:dmList"
	 * Type: Set
	 * Value: channelId (Long/String)들의 집합
	 * TTL: none (필요 시 추후 설정)
	 * Used by: State Server, Service Server, Push Server 등
	 *
	 * 예시 Key: member:100:dmList
	 * 해당 Set은 "멤버가 참여한 DM 채널 아이디" 전체를 저장.
	 * ex) SADD member:100:dmList 1,2,3
	 *
	 * @param memberId 멤버 ID
	 * @return "member:{memberId}:dmList"
	 */
	public static String getMemberDmListKey(Long memberId) {
		return MEMBER_KEY_PREFIX + memberId + MEMBER_DM_LIST_KEY_SUFFIX;
	}

	/**
	 * Key pattern: "member:{memberId}:serverLastInfo"
	 * Type: String
	 * Value: JSON (예: ServerLastInfo)
	 * TTL: none (만료 없이 유지. 추후에 설정)
	 * Used by: State Server, Service Server, Push Server
	 *
	 * 예시 Key: member:100:serverLastInfo
	 * 해당 String은 멤버의 마지막 서버 정보를 JSON 형태로 저장.
	 * ex) SET member:100:serverLastInfo
	 *
	 */
	public static String getServerLastInfoKey(Long serverId) {
		return MEMBER_KEY_PREFIX + serverId + SERVER_LAST_INFO_KEY_SUFFIX;
	}

	/**
	 * Key pattern: "member:{memberId}:friendList"
	 * Type: Set
	 * Value: memberId (Long/String)들의 집합
	 * TTL: none (필요 시 추후 설정)
	 * Used by: State Server, Service Server, Push Server 등
	 *
	 * 예시 Key: member:100:friendList
	 * 해당 Set은 "멤버가 추가한 친구(멤버) 아이디" 전체를 저장.
	 * ex) SADD member:100:friendList 1,2,3
	 *
	 */
	public static String getMemberFriendListKey(Long memberId) {
		return MEMBER_KEY_PREFIX + memberId + MEMBER_FRIEND_LIST_KEY_SUFFIX;
	}

}
