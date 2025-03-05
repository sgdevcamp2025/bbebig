package com.smilegate.bbebig.data.model

import com.smilegate.bbebig.data.response.AttachedFileResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AttachedFileDataModel(
    @SerialName("fileName")
    val fileName: String = "",
    @SerialName("fileSize")
    val fileSize: String = "",
    @SerialName("fileType")
    val fileType: String = "",
    @SerialName("fileUrl")
    val fileUrl: String = "",
)

fun AttachedFileResponse.toDataModel(): AttachedFileDataModel {
    return AttachedFileDataModel(
        fileName = fileName,
        fileSize = fileSize,
        fileType = fileType,
        fileUrl = fileUrl,
    )
}
