package com.smilegate.bbebig.presentation.ui.splash.mvi

import com.smilegate.bbebig.presentation.base.UiState

data class SplashUiState(
    val isLoading: Boolean,
    val isLoginSuccess: Boolean,
) : UiState {
    companion object {
        fun initialize(): SplashUiState {
            return SplashUiState(
                isLoading = false,
                isLoginSuccess = false,
            )
        }
    }
}
