package com.smilegate.bbebig.presentation.ui.home.mvi

import com.smilegate.bbebig.presentation.base.UiSideEffect

sealed interface HomeSideEffect : UiSideEffect {
    data class NavigateToLiveChat(val channelId: Long) : HomeSideEffect
    data object NavigateToChatRoom : HomeSideEffect
    data object NavigateToBack : HomeSideEffect
}
