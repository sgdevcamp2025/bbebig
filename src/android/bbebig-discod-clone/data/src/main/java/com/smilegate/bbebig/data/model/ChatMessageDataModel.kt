package com.smilegate.bbebig.data.model

import com.smilegate.bbebig.data.response.ChatMessageResponse

data class ChatMessageDataModel(
    val channelId: Long,
    val lastMessageId: Long,
    val messages: List<MessageDataModel>,
    val serverId: Long,
    val totalCount: Long,
)

fun ChatMessageResponse.toDataModel(): ChatMessageDataModel {
    return ChatMessageDataModel(
        channelId = channelId,
        lastMessageId = lastMessageId,
        messages = messages.map { it.toDataModel() },
        serverId = serverId,
        totalCount = totalCount,
    )
}
