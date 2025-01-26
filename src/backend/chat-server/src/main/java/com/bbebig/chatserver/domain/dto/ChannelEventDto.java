package com.bbebig.chatserver.domain.dto;

import com.bbebig.chatserver.domain.model.ChannelType;

import java.time.LocalDateTime;

public class ChannelEventDto {

	// ENTER, LEAVE
	private String type;

	// 서버에 있는 채널일 경우
	private Long serverId;

	private Long channelId;

	private ChannelType channelType;

	private LocalDateTime eventTime;
}
