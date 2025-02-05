package com.bbebig.serviceserver.channel.dto.request;

import com.bbebig.serviceserver.channel.entity.ChannelType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ChannelCreateRequestDto {

    @Schema(description = "서버의 ID", example = "1", required = true)
    private final Long serverId;

    @Schema(description = "카테고리의 ID", example = "1|null", required = true)
    private final Long categoryId;

    @Schema(description = "채널의 타입", example = "VOICE|CHAT", required = true)
    private final ChannelType channelType;

    @Schema(description = "채널의 이름", example = "BBeBig의 채널", required = true)
    private final String channelName;

    @Schema(description = "채널의 비공개 여부", example = "true|false", required = true)
    private final boolean privateStatus;

    @Schema(description = "비공개일 경우 초대할 멤버들의 ID", example = "", required = true)
    private final List<Long> memberIds;
}
