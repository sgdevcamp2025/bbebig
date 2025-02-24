package com.smilegate.bbebig.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServerJoinResponse(
    @SerialName("serverId")
    val serverId: Long,
)
