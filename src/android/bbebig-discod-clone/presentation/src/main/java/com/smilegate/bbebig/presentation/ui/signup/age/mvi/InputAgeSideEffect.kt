package com.smilegate.bbebig.presentation.ui.signup.age.mvi

import com.smilegate.bbebig.presentation.base.UiSideEffect

sealed interface InputAgeSideEffect : UiSideEffect {
    data object NavigateToHome : InputAgeSideEffect
    data object NavigateToBack : InputAgeSideEffect
}
