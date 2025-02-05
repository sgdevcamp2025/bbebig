package com.bbebig.serviceserver.channel.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChannelUpdateRequestDto {

    @Schema(description = "채널의 이름", example = "BBebig의 채널", required = true)
    private final String channelName;

    @Schema(description = "채널의 비공개 여부", example = "true/false", required = true)
    private final boolean privateStatus;

    @Schema(description = "비공개일 경우 초대할 멤버들의 ID", example = "", required = true)
    private final List<Long> memberIds;
}
