package com.smilegate.bbebig.presentation.ui.intro

import androidx.lifecycle.SavedStateHandle
import com.smilegate.bbebig.presentation.base.BaseViewModel
import com.smilegate.bbebig.presentation.ui.intro.mvi.IntroIntent
import com.smilegate.bbebig.presentation.ui.intro.mvi.IntroSideEffect
import com.smilegate.bbebig.presentation.ui.intro.mvi.IntroUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class IntroViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
) : BaseViewModel<IntroUiState, IntroSideEffect, IntroIntent>(savedStateHandle) {
    override fun createInitialState(savedStateHandle: SavedStateHandle): IntroUiState {
        return IntroUiState.init()
    }

    public override fun handleIntent(intent: IntroIntent) {
        when (intent) {
            is IntroIntent.ClickLogin -> {
                postSideEffect(IntroSideEffect.NavigateToLogin)
            }

            IntroIntent.ClickSignUp -> {
                postSideEffect(IntroSideEffect.NavigateToSignUp)
            }
        }
    }
}
