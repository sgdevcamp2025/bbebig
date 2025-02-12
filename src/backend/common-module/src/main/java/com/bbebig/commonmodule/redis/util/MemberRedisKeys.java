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

	private static final String MEMBER_DM_CHANNELS_UNREAD_COUNT_KEY_SUFFIX = ":dmChannelsUnreadCount";
	private static final String MEMBER_SERVER_UNREAD_COUNT_KEY_SUFFIX = ":serverUnreadCount";


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
	 * Key pattern: "member:{memberId}:serverUnreadCount"
	 * Type: Hash
	 * Field: serverId (Long)
	 * Value: Json (ServerUnreadCount)
	 * TTL: 3일 (24시간)
	 * Used by: State Server, Service Server 등
	 * TODO : 나중에 참여중인 서버 리스트와 통합 고려
	 *
	 * 예시 Key: member:100:serverUnreadCount
	 * 해당 Hash는 "멤버가 읽지 않은 서버(길드) 메시지 개수"를
	 * (serverId -> ServerUnreadCount) 형태로 저장.
	 * ex) HSET member:100:serverUnreadCount 1 {totalUnreadCount: 3, "channelUnreadCount": {1: 2, 2: 1}}
	 *
	 * @param memberId 멤버 ID
	 * @return "member:{memberId}:serverUnreadCount"
	 */
	public static String getMemberServerUnreadCountKey(Long memberId) {
		return MEMBER_KEY_PREFIX + memberId + MEMBER_SERVER_UNREAD_COUNT_KEY_SUFFIX;
	}

	/**
	 * Key pattern: "member:{memberId}:dmChannelsUnreadCount"
	 * Type: Hash
	 * Field: channelId (Long)
	 * Value: int (UnreadCount)
	 * TTL: 3일 (24시간)
	 * Used by: State Server, Service Server 등
	 *
	 * 예시 Key: member:100:dmChannelsUnreadCount
	 * 해당 Hash는 "멤버가 읽지 않은 DM 채널 메시지 개수"를
	 * (channelId -> UnreadCount) 형태로 저장.
	 * ex) HSET member:100:dmChannelsUnreadCount 1 3
	 *
	 * @param memberId 멤버 ID
	 * @return "member:{memberId}:dmChannelsUnreadCount"
	 */
	public static String getMemberDmChannelsUnreadCountKey(Long memberId) {
		return MEMBER_KEY_PREFIX + memberId + MEMBER_DM_CHANNELS_UNREAD_COUNT_KEY_SUFFIX;
	}

}
