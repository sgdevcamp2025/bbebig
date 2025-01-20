package com.bbebig.chatserver.dto.request;

import com.bbebig.chatserver.model.ChannelType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ChatMessageRequestDto {

	private long id;

	private ChannelType channelType;

	private Long serverId;

	private Long channelId;

	private Long sendMemberId;

	private String content;

	private List<ChatFileDto> attachedFiles;

	private String createdAt;

	private String updatedAt;

	public class ChatFileDto {
		private String fileName;
		private String fileUrl;
		private String fileType;
		private String fileSize;
	}

}
