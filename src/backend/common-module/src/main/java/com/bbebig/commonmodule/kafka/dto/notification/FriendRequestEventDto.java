package com.bbebig.commonmodule.kafka.dto.notification;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper=false)
public class FriendRequestEventDto extends NotificationEventDto {

	private Long friendMemberId;

	private String friendNickName;

	private String friendAvatarUrl;

	private String friendBannerUrl;

	private String status; // SEND, RECEIVE, ACCEPT, REJECT

	@JsonCreator
	public FriendRequestEventDto(
			@JsonProperty("memberId") Long memberId,
			@JsonProperty("type") NotificationEventType type,
			@JsonProperty("friendMemberId") Long friendMemberId,
			@JsonProperty("friendNickName") String friendNickName,
			@JsonProperty("friendAvatarUrl") String friendAvatarUrl,
			@JsonProperty("friendBannerUrl") String friendBannerUrl,
			@JsonProperty("status") String status
	) {
		super(memberId, type);
		this.friendMemberId = friendMemberId;
		this.friendNickName = friendNickName;
		this.friendAvatarUrl = friendAvatarUrl;
		this.friendBannerUrl = friendBannerUrl;
		this.status = status;
	}
}
