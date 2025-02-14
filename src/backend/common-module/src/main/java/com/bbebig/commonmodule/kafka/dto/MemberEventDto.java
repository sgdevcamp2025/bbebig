package com.bbebig.commonmodule.kafka.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MemberEventDto {

	private Long memberId;

	private String type; // CREATE, UPDATE, DELETE

	private String nickname;

	private String profileImageUrl;

	@JsonCreator
	public MemberEventDto(
			@JsonProperty("memberId") Long memberId,
			@JsonProperty("type") String type,
			@JsonProperty("nickname") String nickname,
			@JsonProperty("profileImageUrl") String profileImageUrl
	) {
		this.memberId = memberId;
		this.type = type;
		this.nickname = nickname;
		this.profileImageUrl = profileImageUrl;
	}

}
