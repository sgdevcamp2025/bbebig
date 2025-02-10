package com.bbebig.commonmodule.redis.util;

public class DmRedisKeys {

	// Prefix
	private static final String DM_KEY_PREFIX = "dm:";

	// Suffix
	private static final String MEMBER_DM_LIST_KEY_SUFFIX = ":memberList";
	private static final String MESSAGE_LIST_KEY_SUFFIX = ":messageList";

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
