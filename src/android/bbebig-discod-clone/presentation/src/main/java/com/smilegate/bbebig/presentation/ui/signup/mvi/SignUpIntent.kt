package com.smilegate.bbebig.presentation.ui.signup.mvi

import com.smilegate.bbebig.presentation.base.UiIntent

sealed interface SignUpIntent : UiIntent {
    data class ConfirmAccount(
        val userName: String,
        val password: String,
    ) : SignUpIntent

    data class ConfirmPhoneNumber(
        val phoneNumber: String,
    ) : SignUpIntent

    data class ConfirmNickname(
        val nickname: String,
    ) : SignUpIntent

    data class ConfirmBirth(
        val birth: String,
    ) : SignUpIntent
}
