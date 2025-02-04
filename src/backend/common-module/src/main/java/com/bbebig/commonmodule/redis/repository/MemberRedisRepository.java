package com.bbebig.commonmodule.redis.repository;

import com.bbebig.commonmodule.redis.domain.MemberPresenceStatus;

import java.util.List;
import java.util.Set;

public interface MemberRedisRepository {

	/**
	 * 개별 유저의 참여중인 서버 목록을 Hash 구조로 저장
	 * ex) member:{memberId}:serverList => Set<ServerId>
	 */
	void saveMemberServerSet(Long memberId, List<Long> serverIdList);

	/**
	 * 개별 유저의 참여중인 서버 목록에 서버 추가
	 */
	void addMemberServerToSet(Long memberId, Long serverId);

	/**
	 * 개별 유저의 참여중인 서버 목록에서 서버 삭제
	 */
	void removeMemberServerFromSet(Long memberId, Long serverId);

	/**
	 * 개별 유저의 참여중인 서버 목록이 존재하는지 확인
	 */
	boolean existsMemberServerList(Long memberId);

	/**
	 * 개별 유저의 DM 채널 목록을 Hash 구조로 저장
	 * ex) member:{memberId}:dmList => Set<ChannelId>
	 */
	void saveMemberDmSet(Long memberId, List<Long> channelIdList);
	
	/**
	 * 개별 유저의 DM 채널 목록에 채널 추가
	 */
	void addMemberDmToSet(Long memberId, Long channelId);

	/**
	 * 개별 유저의 DM 채널 목록에서 채널 삭제
	 */
	void removeMemberDmFromSet(Long memberId, Long channelId);

	/**
	 * 개별 유저의 DM 채널 목록이 존재하는지 확인
	 */
	boolean existsMemberDmList(Long memberId);

	/**
	 * 개별 유저의 DM 채널 목록 반환
	 */
	Set<Long> getMemberDmList(Long memberId);

	/**
	 * 개별 유저의 참여중인 서버 목록 반환
	 */
	Set<Long> getMemberServerList(Long memberId);

	/**
	 * 개별 유저의 presence 상태 및 Device Info를 저장
	 * ex) member:{memberId}:memberStatus => MemberPresenceStatus
	 */
	void saveMemberPresenceStatus(Long memberId, MemberPresenceStatus status);

	/**
	 * 개별 유저 presence 상태 정보 조회
	 */
	MemberPresenceStatus getMemberPresenceStatus(Long memberId);

	Long convertToLong(Object obj);
}
