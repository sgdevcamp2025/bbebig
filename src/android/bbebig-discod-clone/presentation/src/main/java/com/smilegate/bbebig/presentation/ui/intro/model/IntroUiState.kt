package com.smilegate.bbebig.presentation.ui.intro.model

import com.smilegate.bbebig.presentation.base.UiState

data class IntroUiState(
    val isLogin: Boolean = false,
) : UiState {
    companion object {
        fun init() = IntroUiState()
    }
}
