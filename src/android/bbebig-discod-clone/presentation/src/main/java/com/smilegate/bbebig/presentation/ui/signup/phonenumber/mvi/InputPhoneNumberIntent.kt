package com.smilegate.bbebig.presentation.ui.signup.phonenumber.mvi

import com.smilegate.bbebig.presentation.base.UiIntent

sealed interface InputPhoneNumberIntent : UiIntent {
    data class ClickConfirm(
        val phoneNumber: Int,
    ) : InputPhoneNumberIntent
}
