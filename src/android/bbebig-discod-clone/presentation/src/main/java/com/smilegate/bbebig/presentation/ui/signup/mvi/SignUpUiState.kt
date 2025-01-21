package com.smilegate.bbebig.presentation.ui.signup.mvi

import com.smilegate.bbebig.presentation.base.UiState

data class SignUpUiState(
    val userName: String,
    val password: String,
    val phoneNumber: Int,
    val nickname: String,
    val birth: String,
) : UiState {
    companion object {
        fun initial() = SignUpUiState(
            userName = "",
            password = "",
            phoneNumber = 0,
            nickname = "",
            birth = "",
        )
    }
}
