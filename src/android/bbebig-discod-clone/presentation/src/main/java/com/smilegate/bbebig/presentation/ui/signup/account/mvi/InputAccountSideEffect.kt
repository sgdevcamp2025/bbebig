package com.smilegate.bbebig.presentation.ui.signup.account.mvi

import com.smilegate.bbebig.presentation.base.UiSideEffect

sealed interface InputAccountSideEffect : UiSideEffect {
    data object NavigatePhoneNumber : InputAccountSideEffect
    data object NavigateToBack : InputAccountSideEffect
}
