package com.smilegate.bbebig.domain.model

import com.smilegate.bbebig.data.model.CategoryInfoDataModel

data class CategoryInfoDomainModel(
    val categoryId: Long,
    val categoryName: String,
    val position: Int,
)

fun CategoryInfoDataModel.toDomainModel(): CategoryInfoDomainModel = CategoryInfoDomainModel(
    categoryId = categoryId,
    categoryName = categoryName,
    position = position,
)

fun List<CategoryInfoDataModel>.toDomainModel(): List<CategoryInfoDomainModel> = map { it.toDomainModel() }
