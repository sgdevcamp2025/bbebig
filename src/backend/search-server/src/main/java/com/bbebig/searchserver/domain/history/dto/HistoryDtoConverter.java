package com.bbebig.searchserver.domain.history.dto;

import com.bbebig.commonmodule.kafka.dto.ChatMessageDto;
import com.bbebig.commonmodule.kafka.dto.model.MessageEventType;
import com.bbebig.searchserver.domain.history.domain.ChannelChatMessage;
import com.bbebig.searchserver.domain.search.domain.ChannelChatMessageElastic;
import com.bbebig.searchserver.domain.history.domain.DmChatMessage;
import com.bbebig.searchserver.domain.search.domain.DmChatMessageElastic;

import java.util.List;

public class HistoryDtoConverter {

	public static ChannelChatMessage convertDtoToChannelChatMessage(ChatMessageDto messageDto) {
		return ChannelChatMessage.builder()
				.id(messageDto.getId())
				.serverId(messageDto.getServerId())
				.channelId(messageDto.getChannelId())
				.sequence(messageDto.getSequence())
				.sendMemberId(messageDto.getSendMemberId())
				.content(messageDto.getContent())
				.attachedFiles(messageDto.getAttachedFiles())
				.createdAt(messageDto.getCreatedAt() != null ? messageDto.getCreatedAt() : null)
				.updatedAt(messageDto.getType() == MessageEventType.MESSAGE_UPDATE ? messageDto.getUpdatedAt() : null)
				.messageType(messageDto.getMessageType())
				.deleted(messageDto.getType() == MessageEventType.MESSAGE_DELETE)
				.build();
	}

	public static DmChatMessage convertDtoToDmChatMessage(ChatMessageDto messageDto) {
		return DmChatMessage.builder()
				.id(messageDto.getId())
				.channelId(messageDto.getChannelId())
				.sequence(messageDto.getSequence())
				.sendMemberId(messageDto.getSendMemberId())
				.content(messageDto.getContent())
				.attachedFiles(messageDto.getAttachedFiles())
				.targetMemberIds(messageDto.getTargetMemberIds())
				.createdAt(messageDto.getCreatedAt() != null ? messageDto.getCreatedAt() : null)
				.updatedAt(messageDto.getType() == MessageEventType.MESSAGE_UPDATE ? messageDto.getUpdatedAt() : null)
				.messageType(messageDto.getMessageType())
				.deleted(messageDto.getType() == MessageEventType.MESSAGE_DELETE)
				.build();
	}

	public static ChannelChatMessageElastic convertDtoToChannelChatMessageElastic(ChatMessageDto messageDto) {
		return ChannelChatMessageElastic.builder()
				.id(messageDto.getId())
				.serverId(messageDto.getServerId())
				.channelId(messageDto.getChannelId())
				.sequence(messageDto.getSequence())
				.sendMemberId(messageDto.getSendMemberId())
				.content(messageDto.getContent())
				.createdAt(messageDto.getCreatedAt() != null ? messageDto.getCreatedAt() : null)
				.build();
	}

	public static DmChatMessageElastic convertDtoToDmChatMessageElastic(ChatMessageDto messageDto) {
		return DmChatMessageElastic.builder()
				.id(messageDto.getId())
				.channelId(messageDto.getChannelId())
				.sequence(messageDto.getSequence())
				.sendMemberId(messageDto.getSendMemberId())
				.content(messageDto.getContent())
				.createdAt(messageDto.getCreatedAt() != null ? messageDto.getCreatedAt() : null)
				.build();
	}

	public static HistoryResponseDto.GetChannelMessageResponseDto convertToGetChannelMessageResponseDto(Long serverId, Long channelId, List<ChannelChatMessage> messages) {
		return HistoryResponseDto.GetChannelMessageResponseDto.builder()
				.serverId(serverId)
				.channelId(channelId)
				.lastMessageId(messages.get(messages.size() - 1).getId())
				.totalCount(messages.size())
				.messages(messages)
				.build();
	}

	public static HistoryResponseDto.GetDmMessageResponseDto convertToGetDmMessageResponseDto(Long channelId, List<DmChatMessage> messages) {
		return HistoryResponseDto.GetDmMessageResponseDto.builder()
				.channelId(channelId)
				.lastMessageId(messages.get(messages.size() - 1).getId())
				.totalCount(messages.size())
				.messages(messages)
				.build();
	}

}
