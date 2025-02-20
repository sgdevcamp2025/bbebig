package com.bbebig.userserver.member.repository;

import com.bbebig.commonmodule.redis.domain.MemberPresenceStatus;
import com.bbebig.commonmodule.redis.domain.ServerLastInfo;
import com.bbebig.commonmodule.redis.repository.MemberRedisRepository;
import com.bbebig.commonmodule.redis.util.MemberRedisKeys;
import com.bbebig.commonmodule.redis.util.MemberRedisTTL;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRedisRepositoryImpl implements MemberRedisRepository {

	private final RedisTemplate<String, Long> redisSetTemplate;
	private final RedisTemplate<String, MemberPresenceStatus> redisMemberStatusTemplate;
	private final RedisTemplate<String, ServerLastInfo> redisServerLastInfoTemplate;

	private SetOperations<String, Long> setOperations;
	private ValueOperations<String, MemberPresenceStatus> valueOperations;
	private HashOperations<String, String, ServerLastInfo> serverLastInfoValueOperations;

	@PostConstruct
	public void initRedisOps() {
		this.setOperations = redisSetTemplate.opsForSet();
		this.valueOperations = redisMemberStatusTemplate.opsForValue();
		this.serverLastInfoValueOperations = redisServerLastInfoTemplate.opsForHash();
	}

	/**
	 * 개별 유저의 참여중인 서버 목록을 Set 구조로 저장
	 * ex) member:{memberId}:serverList => Set<ServerId>
	 */
	@Override
	public void saveMemberServerSet(Long memberId, List<Long> serverIdList) {
		String key = MemberRedisKeys.getMemberServerListKey(memberId);
		setOperations.add(key, serverIdList.toArray(new Long[0]));
	}

	// 개별 유저의 참여중인 서버 목록에 서버 추가
	@Override
	public void addMemberServerToSet(Long memberId, Long serverId) {
		String key = MemberRedisKeys.getMemberServerListKey(memberId);
		setOperations.add(key, serverId);
	}

	// 개별 유저의 참여중인 서버 목록에서 서버 삭제
	@Override
	public void removeMemberServerFromSet(Long memberId, Long serverId) {
		String key = MemberRedisKeys.getMemberServerListKey(memberId);
		setOperations.remove(key, serverId);
	}

	// 개별 유저의 참여중인 서버 목록이 존재하는지 확인
	@Override
	public boolean existsMemberServerList(Long memberId) {
		String key = MemberRedisKeys.getMemberServerListKey(memberId);
		// hasKey는 어떤 템플릿(여기서는 redisSetTemplate)을 써도 됨
		return Boolean.TRUE.equals(redisSetTemplate.hasKey(key));
	}

	// 개별 유저의 참여중인 서버 목록 반환
	@Override
	public Set<Long> getMemberServerList(Long memberId) {
		String key = MemberRedisKeys.getMemberServerListKey(memberId);
		return setOperations.members(key);
	}

	/**
	 * 개별 유저의 DM 채널 목록을 Set 구조로 저장
	 * ex) member:{memberId}:dmList => Set<ChannelId>
	 */
	@Override
	public void saveMemberDmSet(Long memberId, List<Long> channelIdList) {
		String key = MemberRedisKeys.getMemberDmListKey(memberId);
		setOperations.add(key, channelIdList.toArray(new Long[0]));
	}

	// 개별 유저의 DM 채널 목록에 채널 추가
	@Override
	public void addMemberDmToSet(Long memberId, Long channelId) {
		String key = MemberRedisKeys.getMemberDmListKey(memberId);
		setOperations.add(key, channelId);
	}

	// 개별 유저의 DM 채널 목록에서 채널 삭제
	@Override
	public void removeMemberDmFromSet(Long memberId, Long channelId) {
		String key = MemberRedisKeys.getMemberDmListKey(memberId);
		setOperations.remove(key, channelId);
	}

	// 개별 유저의 DM 채널 목록이 존재하는지 확인
	@Override
	public boolean existsMemberDmList(Long memberId) {
		String key = MemberRedisKeys.getMemberDmListKey(memberId);
		return Boolean.TRUE.equals(redisSetTemplate.hasKey(key));
	}

	// 개별 유저의 DM 채널 목록 반환
	@Override
	public Set<Long> getMemberDmList(Long memberId) {
		String key = MemberRedisKeys.getMemberDmListKey(memberId);
		return setOperations.members(key);
	}

	/**
	 * 개별 유저의 presence 상태 및 Device Info를 JSON 형태로 저장
	 * ex) member:{memberId}:memberStatus => MemberPresenceStatus
	 */
	@Override
	public void saveMemberPresenceStatus(Long memberId, MemberPresenceStatus status) {
		String key = MemberRedisKeys.getMemberStatusKey(memberId);
		valueOperations.set(key, status);
	}

	// 개별 유저 presence 상태 정보 조회
	@Override
	public MemberPresenceStatus getMemberPresenceStatus(Long memberId) {
		String key = MemberRedisKeys.getMemberStatusKey(memberId);
		return valueOperations.get(key);
	}

	/**
	 * 개별 유저의 최근 서버 채널 정보를 저장
	 * ex) member:{memberId}:serverLastInfo => ServerLastInfo
	 */
	public void saveServerLastInfo(Long memberId, Long serverId, ServerLastInfo lastInfo) {
		String key = MemberRedisKeys.getServerLastInfoKey(memberId);
		serverLastInfoValueOperations.put(key, serverId.toString(), lastInfo);
		serverLastInfoValueOperations.getOperations().expire(key, MemberRedisTTL.SERVER_LAST_INFO_TTL, TimeUnit.SECONDS);
	}

	// 개별 유저의 최근 서버 채널 정보 조회
	public ServerLastInfo getServerLastInfo(Long memberId, Long serverId) {
		String key = MemberRedisKeys.getServerLastInfoKey(memberId);
		return serverLastInfoValueOperations.get(key, serverId.toString());
	}


	// 개별 유저의 최근 서버 채널 정보 삭제
	public void deleteServerLastInfo(Long memberId, Long serverId) {
		String key = MemberRedisKeys.getServerLastInfoKey(memberId);
		serverLastInfoValueOperations.delete(key, serverId.toString());
	}

	// 개별 유저의 최근 서버 채널 정보 전체 삭제
	public void deleteAllServerLastInfo(Long memberId) {
		String key = MemberRedisKeys.getServerLastInfoKey(memberId);
		redisServerLastInfoTemplate.delete(key);
	}

	// 개별 유저의 최근 서버 채널 정보 전체 조회
	public List<ServerLastInfo> getAllServerLastInfo(Long memberId) {
		String key = MemberRedisKeys.getServerLastInfoKey(memberId);
		return serverLastInfoValueOperations.values(key);
	}

	public boolean existsServerLastInfo(Long memberId, Long serverId) {
		String key = MemberRedisKeys.getMemberServerListKey(memberId);
		return Boolean.TRUE.equals(setOperations.isMember(key, serverId.toString()));
	}

	/**
	 * 개별 유저의 친구 목록을 Set 구조로 저장
	 * ex) member:{memberId}:friendList => Set<MemberId>
	 */
	public void saveMemberFriendSet(Long memberId, List<Long> friendIdList) {
		String key = MemberRedisKeys.getMemberFriendListKey(memberId);
		if (friendIdList == null || friendIdList.isEmpty()) {
			log.warn("saveMemberFriendSet: 저장할 친구 목록이 비어 있음. memberId={}", memberId);
			return; // 빈 값이면 저장 로직 실행하지 않음
		}
		setOperations.add(key, friendIdList.toArray(new Long[0]));
	}

	// 개별 유저의 친구 목록에 친구 추가
	public void addMemberFriendToSet(Long memberId, Long friendId) {
		String key = MemberRedisKeys.getMemberFriendListKey(memberId);
		setOperations.add(key, friendId);
	}

	// 개별 유저의 친구 목록에서 친구 삭제
	public void removeMemberFriendFromSet(Long memberId, Long friendId) {
		String key = MemberRedisKeys.getMemberFriendListKey(memberId);
		setOperations.remove(key, friendId);
	}

	// 개별 유저의 친구 목록이 존재하는지 확인
	public boolean existsMemberFriendList(Long memberId) {
		String key = MemberRedisKeys.getMemberFriendListKey(memberId);
		return Boolean.TRUE.equals(redisSetTemplate.hasKey(key));
	}

	// 개별 유저의 친구 목록 반환
	public Set<Long> getMemberFriendList(Long memberId) {
		String key = MemberRedisKeys.getMemberFriendListKey(memberId);
		return setOperations.members(key);
	}
}
