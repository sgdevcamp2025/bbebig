package com.bbebig.commonmodule.redis;

/**
 * Redis 키 생성 유틸리티.
 * 각 키의 패턴 / 용도 / 자료구조 / TTL / 사용 서비스 등을 정의.
 */
public class RedisKeys {

	// Prefix
	private static final String MEMBER_KEY_PREFIX = "member:";
	private static final String SERVER_KEY_PREFIX = "server:";

	// Suffix
	private static final String MEMBER_STATUS_KEY_SUFFIX = ":memberStatus";
	private static final String SERVER_MEMBER_PRESENCE_STATUS_KEY_SUFFIX = ":serverMemberStatus";
	private static final String SERVER_MEMBER_LIST_KEY_SUFFIX = ":memberList";

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
	 * Key pattern: "server:{serverId}:serverMemberStatus"
	 * Type: Hash
	 * Field: memberId (Long)
	 * Value: JSON (예: ServerMemberStatus)
	 * TTL: none (만료 없이 유지. 추후에 설정)
	 * Used by: State Server , Service Server, Push Server
	 *
	 * 예시 Key: server:1:serverMemberStatus
	 * 해당 Hash는 서버(길드) 내 멤버들의 상태를
	 * (memberId -> ServerMemberStatus) 형태로 관리.
	 *
	 * @param serverId 서버 ID
	 * @return "server:{serverId}:serverMemberStatus"
	 */
	public static String getServerMemberPresenceStatusKey(Long serverId) {
		return SERVER_KEY_PREFIX + serverId + SERVER_MEMBER_PRESENCE_STATUS_KEY_SUFFIX;
	}

	/**
	 * Key pattern: "server:{serverId}:memberList"
	 * Type: Set
	 * Value: memberId (Long/String)들의 집합
	 * TTL: none (필요 시 추후 설정)
	 * Used by: State Server, Service Server, Push Server 등
	 *
	 * 예시 Key: server:1:memberList
	 * 해당 Set은 "서버(길드) 내 멤버 아이디" 전체를 저장.
	 * ex) SADD server:1:memberList 100,101,102
	 *
	 * @param serverId 서버 ID
	 * @return "server:{serverId}:memberList"
	 */
	public static String getServerMemberListKey(Long serverId) {
		return SERVER_KEY_PREFIX + serverId + SERVER_MEMBER_LIST_KEY_SUFFIX;
	}

}
