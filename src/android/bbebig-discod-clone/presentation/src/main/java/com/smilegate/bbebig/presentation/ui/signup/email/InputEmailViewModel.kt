package com.smilegate.bbebig.presentation.ui.signup.email

import androidx.lifecycle.SavedStateHandle
import com.smilegate.bbebig.presentation.base.BaseViewModel
import com.smilegate.bbebig.presentation.ui.signup.email.mvi.InputEmailIntent
import com.smilegate.bbebig.presentation.ui.signup.email.mvi.InputEmailSideEffect
import com.smilegate.bbebig.presentation.ui.signup.email.mvi.InputEmailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InputEmailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<InputEmailUiState, InputEmailSideEffect, InputEmailIntent>(savedStateHandle) {

    override fun createInitialState(savedStateHandle: SavedStateHandle): InputEmailUiState {
        return InputEmailUiState.initial()
    }

    public override fun handleIntent(intent: InputEmailIntent) {
        when (intent) {
            is InputEmailIntent.ClickConfirm -> {
                updateEmail(intent.email)
                postSideEffect(InputEmailSideEffect.NavigateToAuthCode)
            }
        }
    }

    private fun updateEmail(email: String) {
        reduce {
            copy(
                email = email,
            )
        }
    }
}
