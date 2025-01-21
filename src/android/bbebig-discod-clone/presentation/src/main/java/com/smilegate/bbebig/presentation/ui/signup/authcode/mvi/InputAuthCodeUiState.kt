package com.smilegate.bbebig.presentation.ui.signup.authcode.mvi

import com.smilegate.bbebig.presentation.base.UiState

data class InputAuthCodeUiState(
    val authCode: String = "",
    val isLoading: Boolean,
) : UiState {
    companion object {
        fun initial() = InputAuthCodeUiState(
            authCode = "",
            isLoading = false,
        )
    }
}
