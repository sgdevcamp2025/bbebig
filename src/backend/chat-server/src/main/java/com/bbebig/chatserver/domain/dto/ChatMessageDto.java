package com.bbebig.chatserver.domain.dto;

import com.bbebig.chatserver.domain.model.ChannelType;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ChatMessageDto {

	private Long id;
	private ChannelType channelType;
	private MessageType messageType;
	private String type;
	private Long serverId;
	private Long channelId;
	private Long channelSeq;
	private Long sendMemberId;
	private String content;
	private List<ChatFileDto> attachedFiles;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public enum MessageType {
		TEXT, IMAGE, VIDEO, FILE, SYSTEM
	}

}
