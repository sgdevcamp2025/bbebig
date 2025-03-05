package com.smilegate.bbebig.presentation.ui.signup.mvi

import com.smilegate.bbebig.presentation.base.UiSideEffect

sealed interface SignUpSideEffect : UiSideEffect {
    data object NavigateToHome : SignUpSideEffect
    data class ShowErrorToast(val message: String) : SignUpSideEffect
}
