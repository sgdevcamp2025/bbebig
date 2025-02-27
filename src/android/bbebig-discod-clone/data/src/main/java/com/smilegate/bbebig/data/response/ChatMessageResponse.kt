package com.smilegate.bbebig.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatMessageResponse(
    @SerialName("channelId")
    val channelId: Long = 0,
    @SerialName("lastMessageId")
    val lastMessageId: Long = 0,
    @SerialName("messages")
    val messages: List<MessageResponse> = listOf(),
    @SerialName("serverId")
    val serverId: Long = 0,
    @SerialName("totalCount")
    val totalCount: Long = 0,
)
