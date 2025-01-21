package com.smilegate.bbebig.presentation.ui.signup.age

import androidx.lifecycle.SavedStateHandle
import com.smilegate.bbebig.presentation.base.BaseViewModel
import com.smilegate.bbebig.presentation.ui.signup.age.mvi.InputAgeIntent
import com.smilegate.bbebig.presentation.ui.signup.age.mvi.InputAgeSideEffect
import com.smilegate.bbebig.presentation.ui.signup.age.mvi.InputAgeUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InputAgeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<InputAgeUiState, InputAgeSideEffect, InputAgeIntent>(savedStateHandle) {

    override fun createInitialState(savedStateHandle: SavedStateHandle): InputAgeUiState {
        return InputAgeUiState.initial()
    }

    public override fun handleIntent(intent: InputAgeIntent) {
        when (intent) {
            is InputAgeIntent.ClickMakeAccount -> {
                updateBirth(intent.birth)
                postSideEffect(InputAgeSideEffect.NavigateToHome)
            }

            InputAgeIntent.ClickBack -> {
                postSideEffect(InputAgeSideEffect.NavigateToBack)
            }
        }
    }

    private fun updateBirth(birth: String) {
        reduce {
            copy(
                birth = birth,
            )
        }
    }
}
