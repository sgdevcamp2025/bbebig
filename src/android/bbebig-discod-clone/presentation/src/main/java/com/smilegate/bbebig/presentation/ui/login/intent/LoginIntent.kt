package com.smilegate.bbebig.presentation.ui.login.intent

import com.smilegate.bbebig.presentation.base.UiIntent
import com.smilegate.bbebig.presentation.ui.login.model.UserAccount

sealed interface LoginIntent : UiIntent {
    data class ClickLoginConfirm(val accountInfo: UserAccount) : LoginIntent
    data object ClickBack : LoginIntent
}
