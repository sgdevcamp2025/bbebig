package com.bbebig.stateserver.dto;

import com.bbebig.commonmodule.kafka.dto.PresenceEventDto;
import com.bbebig.commonmodule.redis.domain.MemberPresenceStatus;

public class DtoConverter {

	public static PresenceEventDto convertMemberPresenceStatusToPresenceEventDto(MemberPresenceStatus memberPresenceStatus) {
		return PresenceEventDto.builder()
				.memberId(memberPresenceStatus.getMemberId())
				.globalStatus(memberPresenceStatus.getGlobalStatus())
				.actualStatus(memberPresenceStatus.getActualStatus())
				.lastActivityTime(memberPresenceStatus.getLastActivityTime())
				.build();
	}
}
