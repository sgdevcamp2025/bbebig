package com.bbebig.commonmodule.redis.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChannelLastInfo {

	private Long channelId;

	private Long lastReadMessageId;

	private Long lastReadSequence;

	private LocalDateTime lastAccessAt;

	public void update(Long lastReadMessageId, Long lastReadSequence, LocalDateTime lastAccessAt) {
		this.lastReadMessageId = lastReadMessageId;
		this.lastReadSequence = lastReadSequence;
		this.lastAccessAt = lastAccessAt;
	}
}
