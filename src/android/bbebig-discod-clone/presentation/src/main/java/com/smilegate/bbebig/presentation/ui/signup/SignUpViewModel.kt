package com.smilegate.bbebig.presentation.ui.signup

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.smilegate.bbebig.domain.usecase.SignUpUseCase
import com.smilegate.bbebig.presentation.base.BaseViewModel
import com.smilegate.bbebig.presentation.ui.signup.mvi.SignUpIntent
import com.smilegate.bbebig.presentation.ui.signup.mvi.SignUpSideEffect
import com.smilegate.bbebig.presentation.ui.signup.mvi.SignUpUiState
import com.smilegate.bbebig.presentation.ui.signup.mvi.toDomainParam
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val signUpUseCase: SignUpUseCase,
) : BaseViewModel<SignUpUiState, SignUpSideEffect, SignUpIntent>(savedStateHandle) {

    override fun createInitialState(savedStateHandle: SavedStateHandle): SignUpUiState {
        return SignUpUiState.initial()
    }

    public override fun handleIntent(intent: SignUpIntent) {
        when (intent) {
            is SignUpIntent.ConfirmAccount -> {
                updateAccount(
                    userName = intent.userName,
                    password = intent.password,
                )
            }

            is SignUpIntent.ConfirmBirth -> {
                updateBirth(intent.birth)
                makeAccount()
            }

            is SignUpIntent.ConfirmNickname -> {
                updateNickname(intent.nickname)
            }

            is SignUpIntent.ConfirmEmail -> {
                updateEmail(intent.email)
            }
        }
    }

    private fun updateEmail(email: String) {
        Log.d("SignUpViewModel", "updatePhoneNumber: $email")
        reduce {
            copy(
                email = email,
            )
        }
    }

    private fun updateBirth(birth: String) {
        Log.d("SignUpViewModel", "updateBirth: $birth")
        reduce {
            copy(
                birth = birth,
            )
        }
    }

    private fun updateAccount(userName: String, password: String) {
        Log.d("SignUpViewModel", "updateAccount: $userName, $password")
        reduce {
            copy(
                userName = userName,
                password = password,
            )
        }
    }

    private fun updateNickname(nickname: String) {
        reduce {
            copy(
                nickName = nickname,
            )
        }
    }

    private fun makeAccount() {
        viewModelScope.launch {
            signUpUseCase(
                uiState.value.toDomainParam(),
            )
                .onSuccess { postSideEffect(SignUpSideEffect.NavigateToHome) }
                .onFailure {
                    Log.d("SignUpViewModel", "makeAccount: $it")
                    postSideEffect(SignUpSideEffect.ShowErrorToast("계정 만들기 실패"))
                }
        }
    }
}
