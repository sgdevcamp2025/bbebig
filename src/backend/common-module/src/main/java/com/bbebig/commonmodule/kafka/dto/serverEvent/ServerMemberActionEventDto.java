package com.bbebig.commonmodule.kafka.dto.serverEvent;

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

}
