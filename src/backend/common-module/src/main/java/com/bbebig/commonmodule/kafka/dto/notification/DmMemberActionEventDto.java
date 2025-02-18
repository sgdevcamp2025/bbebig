package com.bbebig.commonmodule.kafka.dto.notification;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper=false)
public class DmMemberActionEventDto extends NotificationEventDto {

	private Long targetMemberId;

	private Long channelId;

	private String channelType; // PRIVATE, GROUP

	private String targetMemberNickName;

	private String targetMemberProfileImageUrl;

	private String status; // JOIN, LEAVE, UPDATE

	@JsonCreator
	public DmMemberActionEventDto(
			@JsonProperty("memberId") Long memberId,
			@JsonProperty("type") NotificationEventType type,
			@JsonProperty("targetMemberId") Long targetMemberId,
			@JsonProperty("channelId") Long channelId,
			@JsonProperty("channelType") String channelType,
			@JsonProperty("targetMemberNickName") String targetMemberNickName,
			@JsonProperty("targetMemberProfileImageUrl") String targetMemberProfileImageUrl,
			@JsonProperty("status") String status
	) {
		super(memberId, type);
		this.targetMemberId = targetMemberId;
		this.channelId = channelId;
		this.channelType = channelType;
		this.targetMemberNickName = targetMemberNickName;
		this.targetMemberProfileImageUrl = targetMemberProfileImageUrl;
		this.status = status;
	}
}
