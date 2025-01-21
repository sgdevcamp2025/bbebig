package com.smilegate.bbebig.presentation.ui.signup.phonenumber

import androidx.lifecycle.SavedStateHandle
import com.smilegate.bbebig.presentation.base.BaseViewModel
import com.smilegate.bbebig.presentation.ui.signup.phonenumber.mvi.InputPhoneNumberIntent
import com.smilegate.bbebig.presentation.ui.signup.phonenumber.mvi.InputPhoneNumberSideEffect
import com.smilegate.bbebig.presentation.ui.signup.phonenumber.mvi.InputPhoneNumberUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InputPhoneNumberViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<InputPhoneNumberUiState, InputPhoneNumberSideEffect, InputPhoneNumberIntent>(savedStateHandle) {

    override fun createInitialState(savedStateHandle: SavedStateHandle): InputPhoneNumberUiState {
        return InputPhoneNumberUiState.initial()
    }

    public override fun handleIntent(intent: InputPhoneNumberIntent) {
        when (intent) {
            is InputPhoneNumberIntent.ClickConfirm -> {
                updatePhoneNumber(intent.phoneNumber)
                postSideEffect(InputPhoneNumberSideEffect.NavigateToAuthCode)
            }
        }
    }

    private fun updatePhoneNumber(phoneNumber: Int) {
        reduce {
            copy(
                phoneNumber = phoneNumber.toString(),
            )
        }
    }
}
