package com.bbebig.commonmodule.kafka.dto;

import com.bbebig.commonmodule.kafka.dto.model.ChatType;
import com.bbebig.commonmodule.kafka.dto.model.MessageEventType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ChatMessageDto {

	// UPDATE, DELETE 시 필요
	private Long id; // 메시지 ID (Snowflake로 생성)

	@NotNull(message = "채팅 타입은 null일 수 없습니다.")
	private ChatType chatType;

	@NotNull(message = "메시지 타입은 null일 수 없습니다.")
	private MessageType messageType;

	@NotEmpty(message = "타입(type)은 비어 있을 수 없습니다.")
	private MessageEventType type; // MESSAGE_CREATE, MESSAGE_UPDATE, MESSAGE_DELETE

	// TODO: 추후 서버 채널과 DM 구분할 수 있도록 구현

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

	// CREATE 시 필요
	@PastOrPresent(message = "생성 시간은 현재 시간 이전이어야 합니다.")
	private LocalDateTime createdAt;

	// UPDATE 시 필요
	@PastOrPresent(message = "수정 시간은 현재 시간 이전이어야 합니다.")
	private LocalDateTime updatedAt;

	// @JsonCreator와 @JsonProperty를 사용한 생성자
	@JsonCreator
	public ChatMessageDto(
			@JsonProperty("id") Long id,
			@JsonProperty("chatType") ChatType chatType,
			@JsonProperty("messageType") MessageType messageType,
			@JsonProperty("type") MessageEventType type,
			@JsonProperty("serverId") Long serverId,
			@JsonProperty("channelId") Long channelId,
			@JsonProperty("sendMemberId") Long sendMemberId,
			@JsonProperty("content") String content,
			@JsonProperty("attachedFiles") List<ChatFileDto> attachedFiles,
			@JsonProperty("targetMemberIds") List<Long> targetMemberIds,
			@JsonProperty("createdAt") LocalDateTime createdAt,
			@JsonProperty("updatedAt") LocalDateTime updatedAt
	) {
		this.id = id;
		this.chatType = chatType;
		this.messageType = messageType;
		this.type = type;
		this.serverId = serverId;
		this.channelId = channelId;
		this.sendMemberId = sendMemberId;
		this.content = content;
		this.attachedFiles = attachedFiles;
		this.targetMemberIds = targetMemberIds;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	public enum MessageType {
		TEXT, IMAGE, VIDEO, FILE, SYSTEM
	}
}
