package com.smilegate.bbebig.presentation.ui.signup.account

import androidx.lifecycle.SavedStateHandle
import com.smilegate.bbebig.presentation.base.BaseViewModel
import com.smilegate.bbebig.presentation.ui.signup.account.mvi.InputAccountIntent
import com.smilegate.bbebig.presentation.ui.signup.account.mvi.InputAccountSideEffect
import com.smilegate.bbebig.presentation.ui.signup.account.mvi.InputAccountUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InputAccountViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<InputAccountUiState, InputAccountSideEffect, InputAccountIntent>(savedStateHandle) {
    override fun createInitialState(savedStateHandle: SavedStateHandle): InputAccountUiState {
        return InputAccountUiState.initial()
    }

    public override fun handleIntent(intent: InputAccountIntent) {
        when (intent) {
            is InputAccountIntent.ClickConfirm -> {
                updateAccount(
                    userName = intent.userName,
                    password = intent.password,
                )
                postSideEffect(InputAccountSideEffect.NavigatePhoneNumber)
            }

            is InputAccountIntent.ClickBack -> {
                postSideEffect(InputAccountSideEffect.NavigateToBack)
            }
        }
    }

    private fun updateAccount(userName: String, password: String) {
        reduce {
            copy(
                userName = userName,
                password = password,
            )
        }
    }
}
