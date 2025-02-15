package com.smilegate.bbebig.domain.model.param

import com.smilegate.bbebig.data.request.SignUpRequest

data class SignUpParam(
    val email: String,
    val password: String,
    val name: String,
    val nickName: String,
    val birth: String,
)

fun SignUpParam.toRequestModel() = SignUpRequest(
    email = email,
    password = password,
    name = name,
    nickname = nickName,
    birth = birth,
)
