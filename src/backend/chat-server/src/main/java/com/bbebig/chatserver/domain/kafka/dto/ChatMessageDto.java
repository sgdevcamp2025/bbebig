package com.bbebig.chatserver.domain.kafka.dto;

import com.bbebig.chatserver.domain.chat.model.ChannelType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ChatMessageDto {

	private Long id; // 메시지 ID (Snowflake로 생성)

	@NotNull(message = "채널 타입은 null일 수 없습니다.")
	private ChannelType channelType;

	@NotNull(message = "메시지 타입은 null일 수 없습니다.")
	private MessageType messageType;

	@NotEmpty(message = "타입(type)은 비어 있을 수 없습니다.")
	private String type; // 메시지의 세부 타입 (사용자 정의)

	// 채널 관련 필드
	private Long serverId;

	@NotNull(message = "채널 ID는 null일 수 없습니다.")
	private Long channelId;

	@NotNull(message = "발신자 ID는 null일 수 없습니다.")
	private Long sendMemberId;

	@NotEmpty(message = "메시지 내용(content)은 비어 있을 수 없습니다.")
	private String content;

	private List<ChatFileDto> attachedFiles; // 첨부 파일 목록

	// DM 채널 관련 필드
	private List<Long> targetMemberIds; // 메시지 대상자 목록 (DM일 경우)

	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public enum MessageType {
		TEXT, IMAGE, VIDEO, FILE, SYSTEM
	}
}
