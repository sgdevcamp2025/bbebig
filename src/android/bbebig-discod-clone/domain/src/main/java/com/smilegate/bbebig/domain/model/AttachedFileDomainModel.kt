package com.smilegate.bbebig.domain.model

import com.smilegate.bbebig.data.model.AttachedFileDataModel

data class AttachedFileDomainModel(
    val fileName: String,
    val fileSize: String,
    val fileType: String,
    val fileUrl: String,
)

fun AttachedFileDataModel.toDomainModel(): AttachedFileDomainModel {
    return AttachedFileDomainModel(
        fileName = fileName,
        fileSize = fileSize,
        fileType = fileType,
        fileUrl = fileUrl,
    )
}

fun AttachedFileDomainModel.toDataModel(): AttachedFileDataModel {
    return AttachedFileDataModel(
        fileName = fileName,
        fileSize = fileSize,
        fileType = fileType,
        fileUrl = fileUrl,
    )
}
