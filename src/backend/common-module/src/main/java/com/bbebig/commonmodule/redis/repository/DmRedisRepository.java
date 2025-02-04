package com.bbebig.commonmodule.redis.repository;

import java.util.List;
import java.util.Set;

public interface DmRedisRepository {

	/**
	 * DM 채널에 참여하고 있는 멤버 목록을 Hash 구조로 저장
	 * ex) dm:{channelId}:memberList => Set<MemberId>
	 */
	void saveDmMemberSet(Long channelId, List<Long> memberIdList);

	/**
	 * DM 채널에 참여중인 멤버 목록에 멤버 추가
	 */
	void addDmMemberToSet(Long channelId, Long memberId);

	/**
	 * DM 채널에 참여중인 멤버 목록에서 멤버 삭제
	 */
	void removeDmMemberFromSet(Long channelId, Long memberId);

	/**
	 * DM 채널별 멤버 목록이 존재하는지 확인
	 */
	boolean existsDmMemberList(Long channelId);

	/**
	 * DM 채널에 참여중인 멤버 목록 반환
	 */
	Set<Long> getDmMemberList(Long channelId);

	Long convertToLong(Object obj);

}
