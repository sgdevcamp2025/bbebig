package com.bbebig.searchserver.domain.search.dto;

import com.bbebig.searchserver.domain.search.domain.ChannelChatMessage;
import com.bbebig.searchserver.domain.search.domain.DmChatMessage;
import com.bbebig.searchserver.global.kafka.dto.ChatMessageDto;

public class ChannelChatMessageConverter {

	public static ChannelChatMessage convertDtoToChannelChatMessage(ChatMessageDto messageDto) {
		return ChannelChatMessage.builder()
				.id(messageDto.getId())
				.serverId(messageDto.getServerId())
				.channelId(messageDto.getChannelId())
				.sendMemberId(messageDto.getSendMemberId())
				.content(messageDto.getContent())
				.attachedFiles(messageDto.getAttachedFiles())
				.createdAt(messageDto.getCreatedAt() != null ? messageDto.getCreatedAt() : null)
				.updatedAt(messageDto.getType().equals("MESSAGE_UPDATE") ? messageDto.getUpdatedAt() : null)
				.messageType(messageDto.getMessageType())
				.build();
	}

	public static DmChatMessage convertDtoToDmChatMessage(ChatMessageDto messageDto) {
		return DmChatMessage.builder()
				.id(messageDto.getId())
				.channelId(messageDto.getChannelId())
				.sendMemberId(messageDto.getSendMemberId())
				.content(messageDto.getContent())
				.attachedFiles(messageDto.getAttachedFiles())
				.targetMemberIds(messageDto.getTargetMemberIds())
				.createdAt(messageDto.getCreatedAt() != null ? messageDto.getCreatedAt() : null)
				.updatedAt(messageDto.getType().equals("MESSAGE_UPDATE") ? messageDto.getUpdatedAt() : null)
				.messageType(messageDto.getMessageType())
				.build();
	}

}
