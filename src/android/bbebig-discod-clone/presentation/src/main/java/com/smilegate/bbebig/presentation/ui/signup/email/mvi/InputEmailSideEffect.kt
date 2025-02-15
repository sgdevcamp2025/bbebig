package com.smilegate.bbebig.presentation.ui.signup.email.mvi

import com.smilegate.bbebig.presentation.base.UiSideEffect

sealed interface InputEmailSideEffect : UiSideEffect {
    data object NavigateToAuthCode : InputEmailSideEffect
    data object ShowErrorText : InputEmailSideEffect
}
