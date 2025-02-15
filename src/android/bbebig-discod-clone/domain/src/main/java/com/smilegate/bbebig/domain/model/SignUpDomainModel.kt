package com.smilegate.bbebig.domain.model

import com.smilegate.bbebig.data.model.SignUpDataModel

data class SignUpDomainModel(
    val code: String,
    val message: String,
)

fun SignUpDataModel.toDomainModel() = SignUpDomainModel(
    code = code,
    message = message,
)
