package com.smilegate.bbebig.presentation.ui.signup.mvi

import com.smilegate.bbebig.domain.model.param.SignUpParam
import com.smilegate.bbebig.presentation.base.UiState

data class SignUpUiState(
    val userName: String,
    val password: String,
    val email: String,
    val nickName: String,
    val birth: String,
) : UiState {
    companion object {
        fun initial() = SignUpUiState(
            userName = "",
            password = "",
            email = "",
            nickName = "",
            birth = "",
        )
    }
}

fun SignUpUiState.toDomainParam() = SignUpParam(
    email = email,
    password = password,
    name = userName,
    nickName = nickName,
    birth = birth,
)
