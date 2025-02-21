package com.smilegate.bbebig.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServerInfoResponse(
    @SerialName("categoryInfoList")
    val categoryInfoList: List<CategoryInfoResponse> = emptyList(),
    @SerialName("channelInfoList")
    val channelInfoList: List<ChannelInfoResponse> = emptyList(),
    @SerialName("ownerId")
    val ownerId: Long = 0,
    @SerialName("serverId")
    val serverId: Long = 0,
    @SerialName("serverImageUrl")
    val serverImageUrl: String = "",
    @SerialName("serverName")
    val serverName: String = "",
)
