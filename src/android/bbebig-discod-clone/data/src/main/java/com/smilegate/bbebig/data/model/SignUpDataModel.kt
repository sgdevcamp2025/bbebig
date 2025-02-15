package com.smilegate.bbebig.data.model

import com.smilegate.bbebig.data.response.SignUpResponse

data class SignUpDataModel(
    val code: String,
    val message: String,
)

fun SignUpResponse.toDataModel(): SignUpDataModel = SignUpDataModel(
    code = code,
    message = message,
)
