package com.smilegate.bbebig.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SDP(
    @SerialName("sdp")
    val sdp: String = "",
)