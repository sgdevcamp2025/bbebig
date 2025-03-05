package com.smilegate.bbebig.presentation.ui.signup.nickname.mvi

import com.smilegate.bbebig.presentation.base.UiSideEffect

sealed interface InputNicknameSideEffect : UiSideEffect {
    data object NavigateToAge : InputNicknameSideEffect
    data object ShowErrorText : InputNicknameSideEffect
}
