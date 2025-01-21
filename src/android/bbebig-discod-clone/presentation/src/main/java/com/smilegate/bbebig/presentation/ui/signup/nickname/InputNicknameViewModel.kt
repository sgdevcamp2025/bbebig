package com.smilegate.bbebig.presentation.ui.signup.nickname

import androidx.lifecycle.SavedStateHandle
import com.smilegate.bbebig.presentation.base.BaseViewModel
import com.smilegate.bbebig.presentation.ui.signup.nickname.mvi.InputNicknameIntent
import com.smilegate.bbebig.presentation.ui.signup.nickname.mvi.InputNicknameSideEffect
import com.smilegate.bbebig.presentation.ui.signup.nickname.mvi.InputNicknameUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class InputNicknameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<InputNicknameUiState, InputNicknameSideEffect, InputNicknameIntent>(savedStateHandle) {

    override fun createInitialState(savedStateHandle: SavedStateHandle): InputNicknameUiState {
        return InputNicknameUiState.initial()
    }

    public override fun handleIntent(intent: InputNicknameIntent) {
        when (intent) {
            is InputNicknameIntent.ClickConfirm -> {
                if (checkNickname(intent.nickname)) {
                    updateNickname(intent.nickname)
                    postSideEffect(InputNicknameSideEffect.NavigateToAge)
                } else {
                    postSideEffect(InputNicknameSideEffect.ShowErrorText)
                }
            }
        }
    }

    private fun checkNickname(nickname: String): Boolean {
        // TODO : Check nickname logic
        return true
    }

    private fun updateNickname(nickname: String) {
        reduce {
            copy(
                nickname = nickname,
            )
        }
    }
}
