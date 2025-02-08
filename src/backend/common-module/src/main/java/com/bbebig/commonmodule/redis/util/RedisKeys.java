package com.bbebig.commonmodule.redis.util;

/**
 * Redis 키 생성 유틸리티.
 * 각 키의 패턴 / 용도 / 자료구조 / TTL / 사용 서비스 등을 정의.
 */
public class RedisKeys {

	// Prefix
	private static final String MEMBER_KEY_PREFIX = "member:";
	private static final String SERVER_KEY_PREFIX = "server:";
	private static final String DM_KEY_PREFIX = "dm:";
	private static final String SERVER_CHANNEL_KEY_PREFIX = "serverChannel:";
	// Suffix
	private static final String MEMBER_STATUS_KEY_SUFFIX = ":memberStatus";
	private static final String MEMBER_DM_LIST_KEY_SUFFIX = ":dmList";
	private static final String MEMBER_SERVER_LIST_KEY_SUFFIX = ":serverList";
	private static final String MEMBER_LIST_KEY_SUFFIX = ":memberList";
	private static final String MEMBER_RECENT_SERVER_CHANNELS_KEY_SUFFIX = ":recentServerChannels";
	private static final String MEMBER_RECENT_DM_CHANNELS_KEY_SUFFIX = ":recentDmChannels";
	private static final String MEMBER_DM_CHANNELS_UNREAD_COUNT_KEY_SUFFIX = ":dmChannelsUnreadCount";
	private static final String MEMBER_SERVER_UNREAD_COUNT_KEY_SUFFIX = ":serverUnreadCount";

	private static final String SERVER_MEMBER_PRESENCE_STATUS_KEY_SUFFIX = ":serverMemberStatus";

	private static final String MESSAGE_LIST_KEY_SUFFIX = ":messageList";


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
	 * Key pattern: "member:{memberId}:recentServerChannels"
	 * Type: Hash
	 * Field: channelId (Long)
	 * Value: JSON (예: RecentChannelInfo)
	 * TTL: 3일 (24시간)
	 * Used by: State Server, Service Server 등
	 *
	 * 예시 Key: member:100:recentChannels
	 * 해당 Hash는 "멤버가 최근에 접속한 채널 아이디와 그 정보를
	 * (channelId -> RecentChannelInfo) 형태로 저장.
	 *
	 * @param memberId 멤버 ID
	 * @return "member:{memberId}:recentChannels"
	 */
	public static String getMemberRecentServerChannelsKey(Long memberId) {
		return MEMBER_KEY_PREFIX + memberId + MEMBER_RECENT_SERVER_CHANNELS_KEY_SUFFIX;
	}

	/**
	 * Key pattern: "member:{memberId}:recentDmChannels"
	 * Type: Hash
	 * Field: dmChannelId (Long)
	 * Value: JSON (예: RecentChannelInfo)
	 * TTL: 3일 (24시간)
	 * Used by: State Server, Service Server 등
	 *
	 * 예시 Key: member:100:recentDmChannels
	 * 해당 Hash는 "멤버가 최근에 접속한 DM 채널 아이디와 그 정보를
	 * (channelId -> RecentChannelInfo) 형태로 저장.
	 * ex) HSET member:100:recentDmChannels 1 {lastReadMessageId: 100}
	 *
	 * @param memberId 멤버 ID
	 * @return "member:{memberId}:recentDmChannels"
	 */
	public static String getMemberRecentDmChannelsKey(Long memberId) {
		return MEMBER_KEY_PREFIX + memberId + MEMBER_RECENT_DM_CHANNELS_KEY_SUFFIX;
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
		return SERVER_KEY_PREFIX + serverId + MEMBER_LIST_KEY_SUFFIX;
	}

	/**
	 * Key pattern: "serverChannel:{channelId}:messageList"
	 * Type: List
	 * Value: JSON (예: Message)들의 집합
	 * TTL: 3일 (72시간)
	 * Used by: Search Server, Chat Server
	 *
	 * 예시 Key: serverChannel:1:messageList
	 * 해당 List는 "채널에 저장된 메시지" 상위 300개를 저장.
	 * ex) LPUSH serverChannel:1:messageList {message1}, {message2}, {message3}
	 *
	 * @param channelId 채널 ID
	 * @return "serverChannel:{channelId}:messageList"
	 */
	public static String getChannelMessageListKey(Long channelId) {
		return SERVER_CHANNEL_KEY_PREFIX + channelId + MESSAGE_LIST_KEY_SUFFIX;
	}

	/**
	 * Key pattern: "dm:{channelId}:memberList"
	 * Type: Set
	 * Value: memberId (Long/String)들의 집합
	 * TTL: 1일 (24시간)
	 * Used by: State Server, Service Server, Push Server 등
	 *
	 * 예시 Key: dm:1:memberList
	 * 해당 Set은 "DM 채널 내 멤버 아이디" 전체를 저장.
	 * ex) SADD dm:1:memberList 100,101,102
	 *
	 * @param channelId DM 채널 ID
	 * @return "dm:{channelId}:memberList"
	 */
	public static String getDmMemberListKey(Long channelId) {
		return DM_KEY_PREFIX + channelId + MEMBER_DM_LIST_KEY_SUFFIX;
	}

	/**
	 * Key pattern: "dm:{channelId}:messageList"
	 * Type: List
	 * Value: JSON (예: Message)들의 집합
	 * TTL: 2일 (48시간)
	 * Used by: Search Server, Chat Server
	 *
	 * 예시 Key: dm:1:messageList
	 * 해당 List는 "DM 채널에 저장된 메시지" 상위 100개를 저장.
	 * ex) LPUSH dm:1:messageList {message1}, {message2}, {message3}
	 *
	 * @param channelId DM 채널 ID
	 * @return "dm:{channelId}:messageList"
	 */
	public static String getDmMessageListKey(Long channelId) {
		return DM_KEY_PREFIX + channelId + MESSAGE_LIST_KEY_SUFFIX;
	}

}
