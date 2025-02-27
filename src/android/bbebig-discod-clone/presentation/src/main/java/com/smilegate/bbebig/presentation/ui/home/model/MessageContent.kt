package com.smilegate.bbebig.presentation.ui.home.model

import com.smilegate.bbebig.domain.model.MessageContentDomainModel

data class MessageContent(
    val attachedFiles: List<AttachedFileModel>,
    val content: String,
    val createdAt: String,
    val deleted: Boolean,
    val id: Long,
    val messageType: String,
    val sendMemberId: Long,
    val sequence: Long,
    val updatedAt: String,
    val userInfo: MemberInfo,
) {
    companion object {
        fun toMessage(memberInfo: MemberInfo, chatModel: ChatModel): MessageContent {
            return MessageContent(
                attachedFiles = chatModel.attachedFiles ?: emptyList(),
                content = chatModel.content,
                createdAt = chatModel.createdAt.orEmpty(),
                deleted = false,
                id = chatModel.id ?: -1,
                messageType = chatModel.messageType,
                sendMemberId = chatModel.sendMemberId,
                sequence = chatModel.sequence ?: -1,
                updatedAt = chatModel.updatedAt.orEmpty(),
                userInfo = memberInfo,
            )
        }
    }
}

fun MessageContentDomainModel.toUiModel(): MessageContent {
    return MessageContent(
        attachedFiles = attachedFiles.map { it.toUiModel() },
        content = content,
        createdAt = createdAt,
        deleted = deleted,
        id = id,
        messageType = messageType,
        sendMemberId = sendMemberId,
        sequence = sequence,
        updatedAt = updatedAt,
        userInfo = userInfo.toUiModel(),
    )
}

fun List<MessageContentDomainModel>.toUiModel(): List<MessageContent> {
    return map { it.toUiModel() }
}
