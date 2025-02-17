package com.smilegate.bbebig.domain.model

import com.smilegate.bbebig.data.model.LoginInfoDataModel

data class LoginInfoDomainModel(
    val email: String,
    val password: String,
)

fun LoginInfoDataModel.toDomainModel(): LoginInfoDomainModel = LoginInfoDomainModel(
    email = email,
    password = password,
)

fun LoginInfoDomainModel.toDataModel(): LoginInfoDataModel = LoginInfoDataModel(
    email = email,
    password = password,
)
