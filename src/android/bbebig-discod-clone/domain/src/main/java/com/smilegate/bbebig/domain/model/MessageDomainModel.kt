package com.smilegate.bbebig.domain.model

import com.smilegate.bbebig.data.model.MessageDataModel

data class MessageDomainModel(
    val attachedFiles: List<AttachedFileDomainModel>,
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

fun MessageDataModel.toDomainModel(): MessageDomainModel {
    return MessageDomainModel(
        attachedFiles = attachedFiles.map { it.toDomainModel() },
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
