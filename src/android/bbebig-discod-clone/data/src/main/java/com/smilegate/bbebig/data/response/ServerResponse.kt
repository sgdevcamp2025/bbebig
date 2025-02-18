package com.smilegate.bbebig.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServerResponse(
    @SerialName("serverId")
    val serverId: Int = 0,
    @SerialName("serverImageUrl")
    val serverImageUrl: String? = null,
    @SerialName("serverName")
    val serverName: String = "",
)
