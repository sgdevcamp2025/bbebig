package com.smilegate.bbebig.presentation.base

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<S : UiState, SE : UiSideEffect, I : UiIntent>(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val initialState: S by lazy { createInitialState(savedStateHandle) }

    internal abstract fun createInitialState(savedStateHandle: SavedStateHandle): S

    internal abstract fun handleIntent(intent: I)

    private val _uiState = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = Channel<SE>()
    val sideEffect = _sideEffect.receiveAsFlow()

    // Get current state
    private val currentState: S
        get() = uiState.value

    fun intent(intent: I) {
        viewModelScope.launch {
            handleIntent(intent)
        }
    }

    protected fun reduce(reduce: S.() -> S) {
        val state = currentState.reduce()
        _uiState.update { state }
    }

    protected fun postSideEffect(sideEffect: SE) {
        viewModelScope.launch {
            _sideEffect.send(sideEffect)
        }
    }
}
