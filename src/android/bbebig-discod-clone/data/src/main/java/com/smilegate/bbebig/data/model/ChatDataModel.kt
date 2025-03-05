package com.smilegate.bbebig.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChatDataModel(
    @SerialName("attachedFiles")
    val attachedFiles: List<AttachedFileDataModel>? = listOf(),
    @SerialName("channelId")
    val channelId: Long = 0,
    @SerialName("chatType")
    val chatType: String = "",
    @SerialName("content")
    val content: String = "",
    @SerialName("createdAt")
    val createdAt: String? = "",
    @SerialName("id")
    val id: Long? = 0,
    @SerialName("messageType")
    val messageType: String = "",
    @SerialName("sendMemberId")
    val sendMemberId: Long = 0,
    @SerialName("sequence")
    val sequence: Long? = 0,
    @SerialName("serverId")
    val serverId: Long = 0,
    @SerialName("type")
    val type: String = "",
    @SerialName("updatedAt")
    val updatedAt: String? = "",
)
