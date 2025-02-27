package com.smilegate.bbebig.presentation.ui.home.model

import com.smilegate.bbebig.domain.model.ChatMessageDomainModel

data class ChatMessage(
    val channelId: Long,
    val lastMessageId: Long,
    val messages: List<Message>,
    val serverId: Long,
    val totalCount: Long,
)

fun ChatMessageDomainModel.toUiModel(): ChatMessage {
    return ChatMessage(
        channelId = channelId,
        lastMessageId = lastMessageId,
        messages = messages.map { it.toUiModel() },
        serverId = serverId,
        totalCount = totalCount,
    )
}
