package com.smilegate.bbebig.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ServerListResponse(
    @SerialName("servers")
    val servers: List<ServerResponse> = emptyList(),
)
