package com.smilegate.bbebig.presentation.ui.login.mvi

import com.smilegate.bbebig.presentation.base.UiState
import com.smilegate.bbebig.presentation.model.UserInfo

data class LoginUiState(
    val userInfo: UserInfo,
) : UiState {
    companion object {
        fun init(): LoginUiState = LoginUiState(
            userInfo = UserInfo(
                id = 0,
                password = "",
                name = "",
                email = "",
                phone = "",
            ),
        )
    }
}
