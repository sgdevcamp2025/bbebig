package com.bbebig.commonmodule.redis.util;

public class ServerRedisKeys {

	// Prefix
	private static final String SERVER_KEY_PREFIX = "server:";
	private static final String SERVER_CHANNEL_KEY_PREFIX = "serverChannel:";

	// Suffix
	private static final String MEMBER_LIST_KEY_SUFFIX = ":memberList";
	private static final String SERVER_MEMBER_PRESENCE_STATUS_KEY_SUFFIX = ":serverMemberStatus";
	private static final String SERVER_CHANNEL_LIST_KEY_SUFFIX = ":channelList";

	private static final String MESSAGE_LIST_KEY_SUFFIX = ":messageList";

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
	 * Key pattern: "server:{serverId}:channelList"
	 * Type: Set
	 * Value: channelId (Long/String)들의 집합
	 * TTL: none (필요 시 추후 설정)
	 * Used by: State Server, Service Server, Push Server 등
	 *
	 * 예시 Key: server:1:channelList
	 * 해당 Set은 "서버(길드) 내 채널 아이디" 전체를 저장.
	 * ex) SADD server:1:channelList 100,101,102
	 *
	 * @param serverId 서버 ID
	 * @return "server:{serverId}:channelList"
	 */
	public static String getServerChannelListKey(Long serverId) {
		return SERVER_KEY_PREFIX + serverId + SERVER_CHANNEL_LIST_KEY_SUFFIX;
	}
}
