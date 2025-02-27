package com.smilegate.bbebig.presentation.ui.home.model

import com.smilegate.bbebig.domain.model.AttachedFileDomainModel

data class AttachedFileModel(
    val fileName: String,
    val fileSize: String,
    val fileType: String,
    val fileUrl: String,
)

fun AttachedFileDomainModel.toUiModel(): AttachedFileModel {
    return AttachedFileModel(
        fileName = fileName,
        fileSize = fileSize,
        fileType = fileType,
        fileUrl = fileUrl,
    )
}

fun AttachedFileModel.toDomainModel(): AttachedFileDomainModel {
    return AttachedFileDomainModel(
        fileName = fileName,
        fileSize = fileSize,
        fileType = fileType,
        fileUrl = fileUrl,
    )
}
