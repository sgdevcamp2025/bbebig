package com.smilegate.bbebig.presentation.ui.signup.authcode.mvi

import com.smilegate.bbebig.presentation.base.UiIntent

sealed interface InputAuthCodeIntent : UiIntent {
    data class ClickAuthCodeConfirm(
        val authCode: Int,
    ) : InputAuthCodeIntent
}
