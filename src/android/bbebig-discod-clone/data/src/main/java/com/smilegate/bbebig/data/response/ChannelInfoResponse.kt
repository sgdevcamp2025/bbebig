package com.smilegate.bbebig.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ChannelInfoResponse(
    @SerialName("categoryId")
    val categoryId: Long = 0,
    @SerialName("channelId")
    val channelId: Long = 0,
    @SerialName("channelMemberIdList")
    val channelMemberIdList: List<Long> = emptyList(),
    @SerialName("channelName")
    val channelName: String = "",
    @SerialName("channelType")
    val channelType: String = "",
    @SerialName("position")
    val position: Int = 0,
    @SerialName("privateStatus")
    val privateStatus: Boolean = false,
)
