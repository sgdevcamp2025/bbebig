package com.smilegate.bbebig.presentation.ui.login.sideeffect

import com.smilegate.bbebig.presentation.base.UiSideEffect

sealed interface LoginSideEffect : UiSideEffect {
    data object NavigateToHome : LoginSideEffect
    data object NavigateToBack : LoginSideEffect
}
