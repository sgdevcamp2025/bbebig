package com.smilegate.bbebig.presentation.ui.signup.authcode.mvi

import com.smilegate.bbebig.presentation.base.UiSideEffect

sealed interface InputAuthCodeSideEffect : UiSideEffect {
    data object NavigateToNickname : InputAuthCodeSideEffect
    data object ShowErrorText : InputAuthCodeSideEffect
}
