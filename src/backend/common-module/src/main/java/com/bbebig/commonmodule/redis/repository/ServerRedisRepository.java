package com.bbebig.commonmodule.redis.repository;

import com.bbebig.commonmodule.redis.domain.ServerMemberStatus;

import java.util.List;
import java.util.Set;

public interface ServerRedisRepository {

	/**
	 * 서버에 참여하고 있는 멤버 목록을 Hash 구조로 저장
	 * ex) server:{serverId}:serverMemberList => Set<MemberId>
	 */
	void saveServerMemberSet(Long serverId, List<Long> memberIdList);

	/**
	 * 서버 멤버 추가
	 */
	void addServerMemberToSet(Long serverId, Long memberId);

	/**
	 * 서버 멤버 삭제
	 */
	void removeServerMemberFromSet(Long serverId, Long memberId);

	/**
	 * 서버별 멤버 목록이 존재하는지 확인
	 */
	boolean existsServerMemberList(Long serverId);

	/**
	 * 서버 멤버 목록 반환
	 */
	Set<Long> getServerMemberList(Long serverId);

	/**
	 * 서버 삭제에 따른 서버별 멤버 목록 삭제
	 */
	void deleteServerMemberList(Long serverId);

	/**
	 * 서버별 멤버 상태 정보를 Hash 구조로 저장
	 * (server:{serverId}:serverMemberStatus) => ServerMemberStatus
	 */
	void saveServerMemberPresenceStatus(Long serverId, Long memberId, ServerMemberStatus status);

	/**
	 * 서버 멤버 상태 정보 삭제
	 */
	void removeServerMemberPresenceStatus(Long serverId, Long memberId);

	/**
	 * 서버 멤버 상태 정보 존재 여부 확인
	 */
	boolean existsServerMemberPresenceStatus(Long serverId);

	/**
	 * 서버 멤버 상태 정보 조회
	 */
	ServerMemberStatus getServerMemberStatus(Long serverId, Long memberId);

	/**
	 * 서버별 모든 멤버 상태 정보 조회
	 */
	List<ServerMemberStatus> getAllServerMemberStatus(Long serverId);

	/**
	 * 서버 삭제에 따른 서버별 모든 멤버 상태 정보 삭제
	 */
	void deleteServerMemberStatus(Long serverId);

	/**
	 * 서버별 채널 목록을 Set 구조로 저장
	 * ex) server:{serverId}:channelList => Set<ChannelId>
	 */
	void saveServerChannelSet(Long serverId, List<Long> channelIdList);

	/**
	 * 서버별 채널 추가
	 */
	void addServerChannelToSet(Long serverId, Long channelId);

	/**
	 * 서버별 채널 삭제
	 */
	void removeServerChannelFromSet(Long serverId, Long channelId);

	/**
	 * 서버별 채널 목록이 존재하는지 확인
	 */
	boolean existsServerChannelList(Long serverId);

	/**
	 * 서버별 채널 목록 반환
	 */
	Set<Long> getServerChannelList(Long serverId);

	/**
	 * 서버 삭제에 따른 서버별 채널 목록 삭제
	 */
	void deleteServerChannelList(Long serverId);


	Long convertToLong(Object obj);
}
