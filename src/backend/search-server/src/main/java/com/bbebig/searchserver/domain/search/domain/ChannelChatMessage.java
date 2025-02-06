package com.bbebig.searchserver.domain.search.domain;

import com.bbebig.commonmodule.kafka.dto.*;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

import static com.bbebig.commonmodule.kafka.dto.ChatMessageDto.*;

@Data
@Document(collection = "channel_chat_messages")
@Builder
public class ChannelChatMessage {

	@Id
	private Long id;

	private Long serverId;

	private Long channelId;

	private Long sendMemberId;

	private String content;

	private List<ChatFileDto> attachedFiles;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private MessageType messageType;

	private Boolean isDeleted;

	public void updateContent(String content) {
		this.content = content;
		this.updatedAt = LocalDateTime.now();
	}

	public void delete() {
		this.isDeleted = true;
		this.updatedAt = LocalDateTime.now();
	}

	public boolean isDeleted() {
		return this.isDeleted;
	}
}


