package com.smilegate.bbebig.presentation.ui.signup.account.mvi

import com.smilegate.bbebig.presentation.base.UiState

data class InputAccountUiState(
    val userName: String,
    val password: String,
    val isLoading: Boolean,
) : UiState {
    companion object {
        fun initial() = InputAccountUiState(
            userName = "",
            password = "",
            isLoading = false,
        )
    }
}
