package com.smilegate.bbebig.domain.model

data class MessageContentDomainModel(
    val attachedFiles: List<AttachedFileDomainModel>,
    val content: String,
    val createdAt: String,
    val deleted: Boolean,
    val id: Long,
    val messageType: String,
    val sendMemberId: Long,
    val sequence: Long,
    val updatedAt: String,
    val userInfo: MemberInfoDomainModel,
) {
    companion object {
        fun toDomainModel(
            message: MessageDomainModel,
            userInfo: MemberInfoDomainModel,
        ): MessageContentDomainModel {
            return MessageContentDomainModel(
                attachedFiles = message.attachedFiles,
                content = message.content,
                createdAt = message.createdAt,
                deleted = message.deleted,
                id = message.id,
                messageType = message.messageType,
                sendMemberId = message.sendMemberId,
                sequence = message.sequence,
                updatedAt = message.updatedAt,
                userInfo = userInfo,
            )
        }
    }
}
