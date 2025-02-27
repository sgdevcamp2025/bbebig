package com.smilegate.bbebig.domain.model

import com.smilegate.bbebig.data.model.ChatMessageDataModel

data class ChatMessageDomainModel(
    val channelId: Long,
    val lastMessageId: Long,
    val messages: List<MessageDomainModel>,
    val serverId: Long,
    val totalCount: Long,
)

fun ChatMessageDataModel.toDomainModel(): ChatMessageDomainModel {
    return ChatMessageDomainModel(
        channelId = channelId,
        lastMessageId = lastMessageId,
        messages = messages.map { it.toDomainModel() },
        serverId = serverId,
        totalCount = totalCount,
    )
}
