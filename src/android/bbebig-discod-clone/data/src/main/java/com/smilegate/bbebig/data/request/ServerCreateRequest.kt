package com.smilegate.bbebig.data.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServerCreateRequest(
    @SerialName("serverName")
    val serverName: String,
    @SerialName("serverImageUrl")
    val serverImageUrl: String?,
)
