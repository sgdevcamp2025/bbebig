package com.smilegate.bbebig.presentation.ui.login.mvi

import com.smilegate.bbebig.presentation.base.UiSideEffect

sealed interface LoginSideEffect : UiSideEffect {
    data object NavigateToHome : LoginSideEffect
    data object NavigateToBack : LoginSideEffect
    data object ShowLoginFailToast : LoginSideEffect
    data object ShowLoginInfoSaveFailToast : LoginSideEffect
}
