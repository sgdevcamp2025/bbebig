package com.smilegate.bbebig.presentation.ui.signup.phonenumber.mvi

import com.smilegate.bbebig.presentation.base.UiSideEffect

sealed interface InputPhoneNumberSideEffect : UiSideEffect {
    data object NavigateToAuthCode : InputPhoneNumberSideEffect
    data object ShowErrorText : InputPhoneNumberSideEffect
}
