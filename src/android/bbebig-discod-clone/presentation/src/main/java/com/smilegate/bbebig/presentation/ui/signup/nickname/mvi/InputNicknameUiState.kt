package com.smilegate.bbebig.presentation.ui.signup.nickname.mvi

import com.smilegate.bbebig.presentation.base.UiState

data class InputNicknameUiState(
    val nickname: String,
    val isLong: Boolean,
) : UiState {
    companion object {
        fun initial() = InputNicknameUiState(
            nickname = "",
            isLong = false,
        )
    }
}
