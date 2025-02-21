package com.smilegate.bbebig.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CandidateDataModel(
    @SerialName("candidate")
    val candidate: String = "",
    @SerialName("sdpMid")
    val sdpMid: String = "",
    @SerialName("sdpMLineIndex")
    val sdpMLineIndex: Int = 0,
)