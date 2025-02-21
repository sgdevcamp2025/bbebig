package com.smilegate.bbebig.presentation.ui.home.model

import com.smilegate.bbebig.domain.model.CategoryInfoDomainModel

data class CategoryInfo(
    val categoryId: Long,
    val categoryName: String,
    val position: Int,
)

fun CategoryInfoDomainModel.toUiModel(): CategoryInfo = CategoryInfo(
    categoryId = categoryId,
    categoryName = categoryName,
    position = position,
)

fun List<CategoryInfoDomainModel>.toUiModel(): List<CategoryInfo> = map { it.toUiModel() }
