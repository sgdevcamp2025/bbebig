package com.bbebig.commonmodule.redis.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerLastInfo {

	private Long serverId;

	private Map<Long, ChannelLastInfo> channelLastInfoMap;

	public void addChannelLastInfo(ChannelLastInfo channelLastInfo) {
		this.channelLastInfoMap.put(channelLastInfo.getChannelId(), channelLastInfo);
	}

	public boolean existChannelLastInfo(Long channelId) {
		return this.channelLastInfoMap.containsKey(channelId);
	}

	public void updateChannelLastInfo(Long channelId, Long lastReadMessageId, Long lastReadSequence, LocalDateTime lastAccessAt) {
		ChannelLastInfo channelLastInfo = this.channelLastInfoMap.get(channelId);
		channelLastInfo.setLastReadMessageId(lastReadMessageId);
		channelLastInfo.setLastReadSequence(lastReadSequence);
		channelLastInfo.setLastAccessAt(lastAccessAt);
		this.channelLastInfoMap.put(channelId, channelLastInfo);
	}

	public void removeChannelLastInfo(Long channelId) {
		this.channelLastInfoMap.remove(channelId);
	}
}
