package com.smilegate.bbebig.presentation.ui.home.model

import com.smilegate.bbebig.domain.model.MessageDomainModel

data class Message(
    val attachedFiles: List<AttachedFileModel>,
    val channelId: Long,
    val content: String,
    val createdAt: String,
    val deleted: Boolean,
    val id: Long,
    val messageType: String,
    val sendMemberId: Long,
    val sequence: Long,
    val serverId: Long,
    val updatedAt: String,
)

fun MessageDomainModel.toUiModel(): Message {
    return Message(
        attachedFiles = attachedFiles.map { it.toUiModel() },
        channelId = channelId,
        content = content,
        createdAt = createdAt,
        deleted = deleted,
        id = id,
        messageType = messageType,
        sendMemberId = sendMemberId,
        sequence = sequence,
        serverId = serverId,
        updatedAt = updatedAt,
    )
}

fun ChatModel.toMessage(): Message {
    return Message(
        attachedFiles = attachedFiles ?: emptyList(),
        channelId = channelId,
        content = content,
        createdAt = createdAt.orEmpty(),
        deleted = false,
        id = id ?: -1,
        messageType = messageType,
        sendMemberId = sendMemberId,
        sequence = sequence ?: -1,
        serverId = serverId,
        updatedAt = updatedAt.orEmpty(),
    )
}
