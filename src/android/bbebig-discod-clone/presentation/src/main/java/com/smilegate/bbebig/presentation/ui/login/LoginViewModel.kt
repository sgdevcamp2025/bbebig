package com.smilegate.bbebig.presentation.ui.login

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.smilegate.bbebig.domain.usecase.LoginUseCase
import com.smilegate.bbebig.domain.usecase.SaveLoginInfoUseCase
import com.smilegate.bbebig.domain.usecase.SaveTokenUseCase
import com.smilegate.bbebig.presentation.base.BaseViewModel
import com.smilegate.bbebig.presentation.ui.login.model.UserAccount
import com.smilegate.bbebig.presentation.ui.login.model.toDomainModel
import com.smilegate.bbebig.presentation.ui.login.model.toParam
import com.smilegate.bbebig.presentation.ui.login.mvi.LoginIntent
import com.smilegate.bbebig.presentation.ui.login.mvi.LoginSideEffect
import com.smilegate.bbebig.presentation.ui.login.mvi.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val loginUseCase: LoginUseCase,
    private val saveTokenUseCase: SaveTokenUseCase,
    private val saveLoginInfoUseCase: SaveLoginInfoUseCase,
) : BaseViewModel<LoginUiState, LoginSideEffect, LoginIntent>(savedStateHandle) {
    override fun createInitialState(savedStateHandle: SavedStateHandle): LoginUiState {
        return LoginUiState.init()
    }

    public override fun handleIntent(intent: LoginIntent) {
        when (intent) {
            is LoginIntent.ClickLoginConfirm -> {
                updateLoading(isLoading = true)
                login(intent.accountInfo)
            }

            is LoginIntent.ClickBack -> {
                postSideEffect(LoginSideEffect.NavigateToBack)
            }
        }
    }

    private fun updateLoading(isLoading: Boolean) {
        reduce {
            copy(
                isLoading = isLoading,
            )
        }
    }

    private fun login(userAccount: UserAccount) {
        viewModelScope.launch {
            loginUseCase(userAccount.toParam())
                .onSuccess {
                    saveLoginInfo(userAccount)
                    saveToken(it.accessToken, it.refreshToken)
                }
                .onFailure {
                    postSideEffect(LoginSideEffect.ShowLoginFailToast)
                    updateLoading(isLoading = false)
                }
        }
    }

    private fun saveLoginInfo(userAccount: UserAccount) {
        viewModelScope.launch(Dispatchers.IO) {
            saveLoginInfoUseCase(userAccount.toDomainModel())
                .onFailure {
                    postSideEffect(LoginSideEffect.ShowLoginInfoSaveFailToast)
                    updateLoading(isLoading = false)
                }
        }
    }

    private fun saveToken(accessToken: String, refreshToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            saveTokenUseCase(accessToken = accessToken, refreshToken = refreshToken)
                .onSuccess {
                    postSideEffect(LoginSideEffect.NavigateToHome)
                    updateLoading(isLoading = false)
                }
                .onFailure {
                    postSideEffect(LoginSideEffect.ShowLoginFailToast)
                    updateLoading(isLoading = false)
                }
        }
    }
}
