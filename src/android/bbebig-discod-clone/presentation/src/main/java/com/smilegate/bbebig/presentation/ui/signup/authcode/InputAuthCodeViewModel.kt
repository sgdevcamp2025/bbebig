package com.smilegate.bbebig.presentation.ui.signup.authcode

import androidx.lifecycle.SavedStateHandle
import com.smilegate.bbebig.presentation.base.BaseViewModel
import com.smilegate.bbebig.presentation.ui.signup.authcode.mvi.InputAuthCodeIntent
import com.smilegate.bbebig.presentation.ui.signup.authcode.mvi.InputAuthCodeSideEffect
import com.smilegate.bbebig.presentation.ui.signup.authcode.mvi.InputAuthCodeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InputAuthCodeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<InputAuthCodeUiState, InputAuthCodeSideEffect, InputAuthCodeIntent>(savedStateHandle) {

    override fun createInitialState(savedStateHandle: SavedStateHandle): InputAuthCodeUiState {
        return InputAuthCodeUiState.initial()
    }

    public override fun handleIntent(intent: InputAuthCodeIntent) {
        when (intent) {
            is InputAuthCodeIntent.ClickAuthCodeConfirm -> {
                if (checkAuthCode(intent.authCode)) {
                    postSideEffect(InputAuthCodeSideEffect.NavigateToNickname)
                } else {
                    postSideEffect(InputAuthCodeSideEffect.ShowErrorText)
                }
            }
        }
    }

    private fun checkAuthCode(authCode: Int): Boolean {
        // TODO: Implement auth code check logic
        return true
    }
}
