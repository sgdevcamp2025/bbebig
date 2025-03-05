package com.smilegate.bbebig.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SignUpResponse(
    @SerialName("code")
    val code: String = "",
    @SerialName("message")
    val message: String = "",
)
