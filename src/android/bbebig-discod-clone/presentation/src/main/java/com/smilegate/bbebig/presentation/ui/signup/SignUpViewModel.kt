package com.smilegate.bbebig.presentation.ui.signup

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import com.smilegate.bbebig.presentation.base.BaseViewModel
import com.smilegate.bbebig.presentation.ui.signup.mvi.SignUpIntent
import com.smilegate.bbebig.presentation.ui.signup.mvi.SignUpSideEffect
import com.smilegate.bbebig.presentation.ui.signup.mvi.SignUpUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
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
                if (makeAccount()) {
                    postSideEffect(SignUpSideEffect.NavigateToHome)
                } else {
                    postSideEffect(SignUpSideEffect.ShowErrorToast("계정 만들기 실패"))
                }
            }

            is SignUpIntent.ConfirmNickname -> {
                updateNickname(intent.nickname)
            }

            is SignUpIntent.ConfirmPhoneNumber -> {
                updatePhoneNumber(intent.phoneNumber)
            }
        }
    }

    private fun updatePhoneNumber(phoneNumber: String) {
        Log.d("SignUpViewModel", "updatePhoneNumber: $phoneNumber")
        reduce {
            copy(
                phoneNumber = phoneNumber.toInt(),
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
                nickname = nickname,
            )
        }
    }

    private fun makeAccount(): Boolean {
        // TODO: Implement account creation logic
        return true
    }
}
