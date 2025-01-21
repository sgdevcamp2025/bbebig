package com.smilegate.bbebig.presentation.ui.signup.nickname.mvi

import com.smilegate.bbebig.presentation.base.UiIntent

sealed interface InputNicknameIntent : UiIntent {
    data class ClickConfirm(
        val nickname: String,
    ) : InputNicknameIntent
}
