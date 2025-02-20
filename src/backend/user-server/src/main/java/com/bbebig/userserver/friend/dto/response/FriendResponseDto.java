package com.bbebig.userserver.friend.dto.response;

import com.bbebig.commonmodule.kafka.dto.model.PresenceType;
import com.bbebig.commonmodule.redis.domain.MemberPresenceStatus;
import com.bbebig.userserver.friend.entity.Friend;
import com.bbebig.userserver.member.entity.Member;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

public class FriendResponseDto {

	@Data
	@Builder
	public static class FriendListResponseDto {
		private Long memberId;
		private int friendsCount;
		private List<FriendInfoResponseDto> friends;
	}

	@Data
	@Builder
	public static class PendingFriendListResponseDto {
		private Long memberId;
		private int pendingFriendsCount;
		private int receivePendingFriendsCount;
		private List<PendingFriendInfoResponseDto> sendPendingFriends;
		private List<PendingFriendInfoResponseDto> receivePendingFriends;
	}

	@Data
	@Builder
	public static class FriendInfoResponseDto {
		private Long friendId;
		private Long memberId;
		private String memberName;
		private String memberNickname;
		private String memberAvatarUrl;
		private String memberBannerUrl;
		private String memberIntroduce;
		private String memberEmail;
		private PresenceType globalStatus;
		private LocalDateTime createdAt;
	}

	@Data
	@Builder
	public static class PendingFriendInfoResponseDto {
		private Long friendId;
		private Long memberId;
		private String memberName;
		private String memberNickname;
		private String memberAvatarUrl;
		private String memberBannerUrl;
		private String memberIntroduce;
		private String memberEmail;
		private LocalDateTime createdAt;
	}

	public static FriendInfoResponseDto convertToFriendListResponseDto(Friend friend, Member member, MemberPresenceStatus statusDto) {
		return  FriendInfoResponseDto.builder()
				.friendId(friend.getId())
				.memberId(member.getId())
				.memberName(member.getName())
				.memberNickname(member.getNickname())
				.memberAvatarUrl(member.getAvatarUrl())
				.memberBannerUrl(member.getBannerUrl())
				.memberIntroduce(member.getIntroduce())
				.memberEmail(member.getEmail())
				.globalStatus(statusDto.getGlobalStatus())
				.build();
	}

	public static PendingFriendInfoResponseDto convertToPendingFriendListResponseDto(Friend friend, Member member) {
		return  PendingFriendInfoResponseDto.builder()
				.friendId(friend.getId())
				.memberId(member.getId())
				.memberName(member.getName())
				.memberNickname(member.getNickname())
				.memberAvatarUrl(member.getAvatarUrl())
				.memberBannerUrl(member.getBannerUrl())
				.memberIntroduce(member.getIntroduce())
				.memberEmail(member.getEmail())
				.createdAt(friend.getCreatedAt())
				.build();
	}
}
