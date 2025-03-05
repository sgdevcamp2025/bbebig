package com.smilegate.bbebig.presentation.ui.splash

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.smilegate.bbebig.domain.model.param.LoginParam
import com.smilegate.bbebig.domain.usecase.LoadLoginInfoUseCase
import com.smilegate.bbebig.domain.usecase.LoginUseCase
import com.smilegate.bbebig.presentation.base.BaseViewModel
import com.smilegate.bbebig.presentation.ui.splash.mvi.SplashIntent
import com.smilegate.bbebig.presentation.ui.splash.mvi.SplashSideEffect
import com.smilegate.bbebig.presentation.ui.splash.mvi.SplashUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val loadLoginInfoUseCase: LoadLoginInfoUseCase,
    private val loginUseCase: LoginUseCase,
) : BaseViewModel<SplashUiState, SplashSideEffect, SplashIntent>(savedStateHandle) {

    init {
        viewModelScope.launch {
            delay(500L)
            updateLoading(isLoading = true)
            checkLoginInfo()
        }
    }

    private fun updateLoading(isLoading: Boolean) {
        reduce {
            copy(isLoading = isLoading)
        }
    }

    private fun checkLoginInfo() {
        viewModelScope.launch {
            loadLoginInfoUseCase()
                .onSuccess {
                    login(email = it.email, password = it.password)
                }
                .onFailure {
                    updateLoginState(isSuccess = false)
                    postSideEffect(SplashSideEffect.NavigateToIntro)
                }
        }
    }

    private fun login(email: String, password: String) {
        viewModelScope.launch {
            loginUseCase(LoginParam(email, password))
                .onSuccess {
                    updateLoginState(isSuccess = true)
                    postSideEffect(SplashSideEffect.NavigateToHome)
                }
                .onFailure {
                    updateLoginState(isSuccess = false)
                    postSideEffect(SplashSideEffect.NavigateToIntro)
                }
            updateLoading(isLoading = false)
        }
    }

    private fun updateLoginState(isSuccess: Boolean) {
        reduce {
            copy(
                isLoginSuccess = isSuccess,
            )
        }
    }

    override fun createInitialState(savedStateHandle: SavedStateHandle): SplashUiState {
        return SplashUiState.initialize()
    }

    override fun handleIntent(intent: SplashIntent) = Unit
}
