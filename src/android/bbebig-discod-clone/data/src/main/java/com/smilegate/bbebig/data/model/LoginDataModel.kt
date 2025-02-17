package com.smilegate.bbebig.data.model

import com.smilegate.bbebig.data.response.LoginResponse

data class LoginDataModel(
    val accessToken: String,
    val refreshToken: String,
)

fun LoginResponse.toDataModel(): LoginDataModel = LoginDataModel(
    accessToken = accessToken,
    refreshToken = refreshToken,
)
