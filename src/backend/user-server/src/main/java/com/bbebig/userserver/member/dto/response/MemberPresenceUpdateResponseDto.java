package com.bbebig.userserver.member.dto.response;

import com.bbebig.commonmodule.kafka.dto.model.PresenceType;
import com.bbebig.userserver.member.entity.Member;
import com.bbebig.userserver.member.entity.CustomPresenceStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberPresenceUpdateResponseDto {

    private final Long id;
    private final PresenceType customPresenceStatus;

    public static MemberPresenceUpdateResponseDto convertToMemberPresenceUpdateResponseDto(Member member) {
        return MemberPresenceUpdateResponseDto.builder()
                .id(member.getId())
                .customPresenceStatus(member.getCustomPresenceStatus())
                .build();
    }
}
