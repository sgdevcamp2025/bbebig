package com.smilegate.bbebig.presentation.ui.signup.email.mvi

import com.smilegate.bbebig.presentation.base.UiState

data class InputEmailUiState(
    val isLoading: Boolean,
    val email: String,
) : UiState {
    companion object {
        fun initial() = InputEmailUiState(
            email = "",
            isLoading = false,
        )
    }
}
