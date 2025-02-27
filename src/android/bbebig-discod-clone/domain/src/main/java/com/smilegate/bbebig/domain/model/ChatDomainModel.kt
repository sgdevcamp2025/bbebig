package com.smilegate.bbebig.domain.model

import com.smilegate.bbebig.data.model.ChatDataModel

data class ChatDomainModel(
    val attachedFiles: List<AttachedFileDomainModel>?,
    val channelId: Long,
    val chatType: String,
    val content: String,
    val createdAt: String?,
    val id: Long?,
    val messageType: String,
    val sendMemberId: Long,
    val sequence: Long?,
    val serverId: Long,
    val type: String,
    val updatedAt: String?,
)
fun ChatDataModel.toChatDomainModel(): ChatDomainModel {
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

fun ChatDomainModel.toChatDataModel(): ChatDataModel {
    return ChatDataModel(
        attachedFiles = attachedFiles?.map { it.toDataModel() },
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
