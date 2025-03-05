package com.smilegate.bbebig.data.model

import com.smilegate.bbebig.data.response.CategoryInfoResponse

data class CategoryInfoDataModel(
    val categoryId: Long,
    val categoryName: String,
    val position: Int,
)

fun CategoryInfoResponse.toDataModel() = CategoryInfoDataModel(
    categoryId = categoryId,
    categoryName = categoryName,
    position = position,
)

fun List<CategoryInfoResponse>.toDataModel() = map { it.toDataModel() }
