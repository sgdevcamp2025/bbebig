package com.bbebig.searchserver.domain.history.domain;

import com.bbebig.commonmodule.kafka.dto.*;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

import static com.bbebig.commonmodule.kafka.dto.ChatMessageDto.*;

@Data
@Document(collection = "channel_chat_messages")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChannelChatMessage {

	@Id
	private Long id;

	private Long serverId;

	private Long channelId;

	private Long sequence;

	private Long sendMemberId;

	private String content;

	private List<ChatFileDto> attachedFiles;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private MessageType messageType;

	@Builder.Default
	private Boolean deleted = false;

	public void updateContent(String content) {
		this.content = content;
		this.updatedAt = LocalDateTime.now();
	}

	public void delete() {
		this.deleted = true;
		this.updatedAt = LocalDateTime.now();
	}

	public boolean isDeleted() {
		return deleted != null ? deleted : false; // null이면 기본값 false 반환
	}
}


