package com.bbebig.searchserver.domain.search.domain;

import com.bbebig.searchserver.global.kafka.dto.ChatFileDto;
import com.bbebig.searchserver.global.kafka.model.MessageType;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "dm_chat_messages")
@Builder
public class DmChatMessage {

	@Id
	private Long id;

	private Long channelId;

	private Long sendMemberId;

	private String content;

	private List<ChatFileDto> attachedFiles;

	private List<Long> targetMemberIds;

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
