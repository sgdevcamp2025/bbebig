package com.smilegate.bbebig.domain.model

import com.smilegate.bbebig.data.model.LoginDataModel

data class LoginDomainModel(
    val accessToken: String,
    val refreshToken: String,
)

fun LoginDataModel.toDomainModel(): LoginDomainModel = LoginDomainModel(
    accessToken = accessToken,
    refreshToken = refreshToken,
)
