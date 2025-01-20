package com.bbebig.chatserver.dto;

import com.bbebig.chatserver.model.ChannelType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ChatMessageDto {

	private long id;

	private ChannelType channelType;

	private MessageType messageType;

	private Long serverId;

	private Long channelId;

	private Long sendMemberId;

	private String content;

	private List<ChatFileDto> attachedFiles;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	public enum MessageType {
		TEXT, IMAGE, VIDEO, FILE, SYSTEM
	}

}
