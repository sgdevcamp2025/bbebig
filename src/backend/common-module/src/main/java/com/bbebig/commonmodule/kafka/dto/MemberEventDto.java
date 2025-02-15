package com.bbebig.commonmodule.kafka.dto;

import com.bbebig.commonmodule.kafka.dto.model.PresenceType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberEventDto {

	private Long memberId;

	private String type; // CREATE, UPDATE, DELETE, PRESENCE_UPDATE

	private String nickname;

	private String avatarUrl;

	private String bannerUrl;

	private PresenceType globalStatus; // PRESENCE_UPDATE인 경우에만 사용

	@JsonCreator
	public MemberEventDto(
			@JsonProperty("memberId") Long memberId,
			@JsonProperty("type") String type,
			@JsonProperty("nickname") String nickname,
			@JsonProperty("avatarUrl") String avatarUrl,
			@JsonProperty("bannerUrl") String bannerUrl,
			@JsonProperty("globalStatus") PresenceType globalStatus
	) {
		this.memberId = memberId;
		this.type = type;
		this.nickname = nickname;
		this.avatarUrl = avatarUrl;
		this.bannerUrl = bannerUrl;
		this.globalStatus = globalStatus;
	}

}
