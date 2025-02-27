package com.smilegate.bbebig.data.model

import com.smilegate.bbebig.data.response.MessageResponse

data class MessageDataModel(
    val attachedFiles: List<AttachedFileDataModel>,
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

fun MessageResponse.toDataModel(): MessageDataModel {
    return MessageDataModel(
        attachedFiles = attachedFiles.map { it.toDataModel() },
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
