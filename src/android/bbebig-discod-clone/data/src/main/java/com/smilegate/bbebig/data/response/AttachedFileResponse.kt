package com.smilegate.bbebig.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AttachedFileResponse(
    @SerialName("fileName")
    val fileName: String = "",
    @SerialName("fileSize")
    val fileSize: String = "",
    @SerialName("fileType")
    val fileType: String = "",
    @SerialName("fileUrl")
    val fileUrl: String = "",
)
