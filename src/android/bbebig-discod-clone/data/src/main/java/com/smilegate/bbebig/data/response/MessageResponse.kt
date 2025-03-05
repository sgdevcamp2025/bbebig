package com.smilegate.bbebig.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MessageResponse(
    @SerialName("attachedFiles")
    val attachedFiles: List<AttachedFileResponse> = listOf(),
    @SerialName("channelId")
    val channelId: Long = 0,
    @SerialName("content")
    val content: String = "",
    @SerialName("createdAt")
    val createdAt: String = "",
    @SerialName("deleted")
    val deleted: Boolean = false,
    @SerialName("id")
    val id: Long = 0,
    @SerialName("messageType")
    val messageType: String = "",
    @SerialName("sendMemberId")
    val sendMemberId: Long = 0,
    @SerialName("sequence")
    val sequence: Long = 0,
    @SerialName("serverId")
    val serverId: Long = 0,
    @SerialName("updatedAt")
    val updatedAt: String = "",
)
