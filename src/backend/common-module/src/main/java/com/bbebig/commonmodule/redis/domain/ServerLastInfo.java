package com.bbebig.commonmodule.redis.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServerLastInfo {

	private Long serverId;

	private List<ChannelLastInfo> channelLastInfoList;

	public void addChannelLastInfo(ChannelLastInfo channelLastInfo) {
		this.channelLastInfoList.add(channelLastInfo);
	}

	public boolean existChannelLastInfo(Long channelId) {
		return this.channelLastInfoList.stream().anyMatch(channelLastInfo -> channelLastInfo.getChannelId().equals(channelId));
	}

	public void updateChannelLastInfo(Long channelId, Long lastReadMessageId, LocalDateTime lastAccessAt) {
		for (ChannelLastInfo channelLastInfo : this.channelLastInfoList) {
			if (channelLastInfo.getChannelId().equals(channelId)) {
				channelLastInfo.update(lastReadMessageId, lastAccessAt);
				return;
			}
		}
	}

	public void removeChannelLastInfo(Long channelId) {
		this.channelLastInfoList.removeIf(channelLastInfo -> channelLastInfo.getChannelId().equals(channelId));
	}
}
