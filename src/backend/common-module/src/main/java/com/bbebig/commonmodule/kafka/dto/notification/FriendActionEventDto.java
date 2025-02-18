package com.bbebig.commonmodule.kafka.dto.notification;

import com.bbebig.commonmodule.kafka.dto.notification.status.FriendActionStatus;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper=false)
public class FriendActionEventDto extends NotificationEventDto {

	private Long friendId;

	private Long friendMemberId;

	private FriendActionStatus status;

	private String friendNickName;

	private String friendAvatarUrl;

	private String friendBannerUrl;

	@JsonCreator
	public FriendActionEventDto(
			@JsonProperty("memberId") Long memberId,
			@JsonProperty("type") NotificationEventType type,
			@JsonProperty("friendMemberId") Long friendMemberId,
			@JsonProperty("status") FriendActionStatus status,
			@JsonProperty("friendNickName") String friendNickName,
			@JsonProperty("friendAvatarUrl") String friendAvatarUrl,
			@JsonProperty("friendBannerUrl") String friendBannerUrl

	) {
		super(memberId, type);
		this.friendMemberId = friendMemberId;
		this.status = status;
		this.friendNickName = friendNickName;
		this.friendAvatarUrl = friendAvatarUrl;
		this.friendBannerUrl = friendBannerUrl;
	}
}
