package com.smilegate.bbebig.presentation.ui.signup.age.mvi

import com.smilegate.bbebig.presentation.base.UiState

data class InputAgeUiState(
    val isLoading: Boolean,
    val birth: String,
) : UiState {
    companion object {
        fun initial() = InputAgeUiState(
            birth = "",
            isLoading = false,
        )
    }
}
