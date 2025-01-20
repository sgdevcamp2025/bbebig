package com.smilegate.bbebig.presentation.ui.intro.mvi

import com.smilegate.bbebig.presentation.base.UiState

data class IntroUiState(
    val isLogin: Boolean = false,
) : UiState {
    companion object {
        fun init() = IntroUiState()
    }
}
