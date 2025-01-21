package com.smilegate.bbebig.presentation.ui.signup.account.mvi

import com.smilegate.bbebig.presentation.base.UiIntent

sealed interface InputAccountIntent : UiIntent {
    data class ClickConfirm(
        val userName: String,
        val password: String,
    ) : InputAccountIntent

    data object ClickBack : InputAccountIntent
}
