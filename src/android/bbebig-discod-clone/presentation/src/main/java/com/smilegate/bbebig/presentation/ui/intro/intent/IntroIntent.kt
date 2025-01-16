package com.smilegate.bbebig.presentation.ui.intro.intent

import com.smilegate.bbebig.presentation.base.UiIntent

sealed interface IntroIntent : UiIntent {
    data object ClickLogin : IntroIntent
    data object ClickSignUp : IntroIntent
}
