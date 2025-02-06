package com.bbebig.searchserver.domain.search.dto;

import com.bbebig.commonmodule.kafka.dto.ChatMessageDto;
import com.bbebig.searchserver.domain.search.domain.ChannelChatMessage;
import com.bbebig.searchserver.domain.search.domain.ChannelChatMessageElastic;
import com.bbebig.searchserver.domain.search.domain.DmChatMessage;
import com.bbebig.searchserver.domain.search.domain.DmChatMessageElastic;

public class ChatMessageDtoConverter {

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

	public static ChannelChatMessageElastic convertDtoToChannelChatMessageElastic(ChatMessageDto messageDto) {
		return ChannelChatMessageElastic.builder()
				.id(messageDto.getId())
				.serverId(messageDto.getServerId())
				.channelId(messageDto.getChannelId())
				.sendMemberId(messageDto.getSendMemberId())
				.content(messageDto.getContent())
				.createdAt(messageDto.getCreatedAt() != null ? messageDto.getCreatedAt() : null)
				.build();
	}

	public static DmChatMessageElastic convertDtoToDmChatMessageElastic(ChatMessageDto messageDto) {
		return DmChatMessageElastic.builder()
				.id(messageDto.getId())
				.channelId(messageDto.getChannelId())
				.sendMemberId(messageDto.getSendMemberId())
				.content(messageDto.getContent())
				.createdAt(messageDto.getCreatedAt() != null ? messageDto.getCreatedAt() : null)
				.build();
	}

}
