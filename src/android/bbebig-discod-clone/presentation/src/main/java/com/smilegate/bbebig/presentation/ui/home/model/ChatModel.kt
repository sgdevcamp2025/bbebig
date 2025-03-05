package com.smilegate.bbebig.presentation.ui.home.model

import com.smilegate.bbebig.domain.model.ChatDomainModel

data class ChatModel(
    val attachedFiles: List<AttachedFileModel>? = null,
    val channelId: Long,
    val chatType: String,
    val content: String,
    val createdAt: String? = null,
    val id: Long? = null,
    val messageType: String,
    val sendMemberId: Long,
    val sequence: Long? = null,
    val serverId: Long,
    val type: String,
    val updatedAt: String? = null,
)

fun ChatDomainModel.toUiModel(): ChatModel {
    return ChatModel(
        attachedFiles = attachedFiles?.map { it.toUiModel() },
        channelId = channelId,
        chatType = chatType,
        content = content,
        createdAt = createdAt,
        id = id,
        messageType = messageType,
        sendMemberId = sendMemberId,
        sequence = sequence,
        serverId = serverId,
        type = type,
        updatedAt = updatedAt,
    )
}

fun ChatModel.toDomainModel(): ChatDomainModel {
    return ChatDomainModel(
        attachedFiles = attachedFiles?.map { it.toDomainModel() },
        channelId = channelId,
        chatType = chatType,
        content = content,
        createdAt = createdAt,
        id = id,
        messageType = messageType,
        sendMemberId = sendMemberId,
        sequence = sequence,
        serverId = serverId,
        type = type,
        updatedAt = updatedAt,
    )
}
