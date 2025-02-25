package com.smilegate.bbebig.presentation.ui.splash.mvi

import com.smilegate.bbebig.presentation.base.UiSideEffect

sealed interface SplashSideEffect : UiSideEffect {
    data object NavigateToHome : SplashSideEffect
    data object NavigateToIntro : SplashSideEffect
}
