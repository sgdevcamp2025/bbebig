package com.bbebig.commonmodule.redis.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class RecentServerChannelInfo {

	private Long channelId;

	private LocalDateTime lastAccessTime;

	private Long lastReadMessageId;
}
