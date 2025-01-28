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
}
