package com.bbebig.commonmodule.kafka.dto.serverEvent;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper=false)
public class ServerMemberActionEventDto extends ServerEventDto {

	private Long memberId;

	private String nickname;

	private String profileImageUrl;

	private String status; // JOIN, LEAVE, UPDATE

	@JsonCreator
	public ServerMemberActionEventDto(
			@JsonProperty("serverId") Long serverId,
			@JsonProperty("serverEventType") ServerEventType serverEventType,
			@JsonProperty("memberId") Long memberId,
			@JsonProperty("nickname") String nickname,
			@JsonProperty("profileImageUrl") String profileImageUrl,
			@JsonProperty("status") String status
	) {
		super(serverId, serverEventType);
		this.memberId = memberId;
		this.nickname = nickname;
		this.profileImageUrl = profileImageUrl;
		this.status = status;
	}

}
