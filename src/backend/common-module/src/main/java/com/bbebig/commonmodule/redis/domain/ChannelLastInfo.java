package com.bbebig.commonmodule.redis.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class ChannelLastInfo {

	private Long channelId;

	private Long lastReadMessageId;

	private LocalDateTime lastAccessAt;

	public void update(Long lastReadMessageId, LocalDateTime lastAccessAt) {
		this.lastReadMessageId = lastReadMessageId;
		this.lastAccessAt = lastAccessAt;
	}
}
