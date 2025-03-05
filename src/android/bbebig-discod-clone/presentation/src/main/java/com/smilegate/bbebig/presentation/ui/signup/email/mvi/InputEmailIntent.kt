package com.smilegate.bbebig.presentation.ui.signup.email.mvi

import com.smilegate.bbebig.presentation.base.UiIntent

sealed interface InputEmailIntent : UiIntent {
    data class ClickConfirm(val email: String) : InputEmailIntent
}
