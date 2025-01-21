package com.smilegate.bbebig.presentation.ui.signup.age.mvi

import com.smilegate.bbebig.presentation.base.UiIntent

sealed interface InputAgeIntent : UiIntent {
    data class ClickMakeAccount(
        val birth: String,
    ) : InputAgeIntent

    data object ClickBack : InputAgeIntent
}
