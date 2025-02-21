package com.smilegate.bbebig.data.response

import com.smilegate.bbebig.data.model.CategoryInfoDataModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CategoryInfoResponse(
    @SerialName("categoryId")
    val categoryId: Long = 0,
    @SerialName("categoryName")
    val categoryName: String = "",
    @SerialName("position")
    val position: Int = 0,
)

fun CategoryInfoResponse.toDataModel() = CategoryInfoDataModel(
    categoryId = categoryId,
    categoryName = categoryName,
    position = position,
)
