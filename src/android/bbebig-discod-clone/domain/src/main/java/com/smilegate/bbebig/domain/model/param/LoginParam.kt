package com.smilegate.bbebig.domain.model.param

import com.smilegate.bbebig.data.request.LoginRequest

data class LoginParam(
    val email: String,
    val password: String,
)

fun LoginParam.toRequestModel(): LoginRequest {
    return LoginRequest(
        email = email,
        password = password,
    )
}
