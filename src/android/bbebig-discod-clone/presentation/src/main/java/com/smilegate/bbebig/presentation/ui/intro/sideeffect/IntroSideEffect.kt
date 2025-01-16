package com.smilegate.bbebig.presentation.ui.intro.sideeffect

import com.smilegate.bbebig.presentation.base.UiSideEffect

sealed interface IntroSideEffect : UiSideEffect {
    data object NavigateToLogin : IntroSideEffect
    data object NavigateToSignUp : IntroSideEffect
}
