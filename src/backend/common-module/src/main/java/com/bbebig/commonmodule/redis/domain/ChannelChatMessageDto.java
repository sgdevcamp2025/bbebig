package com.bbebig.commonmodule.redis.domain;

import com.bbebig.commonmodule.kafka.dto.ChatFileDto;
import com.bbebig.commonmodule.kafka.dto.ChatMessageDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChannelChatMessageDto {

	private Long id;

	private Long serverId;

	private Long channelId;

	private Long sendMemberId;

	private String content;

	private List<ChatFileDto> attachedFiles;

	private LocalDateTime createdAt;

	private LocalDateTime updatedAt;

	private ChatMessageDto.MessageType messageType;

	private Boolean isDeleted;
}
