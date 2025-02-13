package com.bbebig.userserver.friend.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FriendCreateRequestDto {

    @Schema(description = "보내는 사람 ID", example = "1", required = true)
    private final Long toMemberId;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public FriendCreateRequestDto(@JsonProperty("toMemberId") Long toMemberId) {
        this.toMemberId = toMemberId;
    }
}