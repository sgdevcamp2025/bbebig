package com.bbebig.userserver.member.dto.response;

import com.bbebig.userserver.member.entity.Member;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberDeleteResponseDto {

    private final Long id;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public MemberDeleteResponseDto(@JsonProperty("id") Long id) {
        this.id = id;
    }

    public static MemberDeleteResponseDto convertToMemberDeleteResponseDto(Member member) {
        return MemberDeleteResponseDto.builder()
                .id(member.getId())
                .build();
    }
}
