package com.smilegate.bbebig.presentation.ui.login

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.smilegate.bbebig.presentation.base.BaseViewModel
import com.smilegate.bbebig.presentation.ui.login.intent.LoginIntent
import com.smilegate.bbebig.presentation.ui.login.model.LoginUiState
import com.smilegate.bbebig.presentation.ui.login.sideeffect.LoginSideEffect
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<LoginUiState, LoginSideEffect, LoginIntent>(savedStateHandle) {
    override fun createInitialState(savedStateHandle: SavedStateHandle): LoginUiState {
        return LoginUiState.init()
    }

    public override fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.ClickLoginConfirm -> {
                // TODO: 향후 로그인 로직 추가
                val userAccount = intent.accountInfo
                Log.d("LoginViewModel", "email: ${userAccount.email}, password: ${userAccount.password}")
                postSideEffect(LoginSideEffect.NavigateToHome)
            }

            is LoginIntent.ClickBack -> {
                postSideEffect(LoginSideEffect.NavigateToBack)
            }
        }
    }
}
