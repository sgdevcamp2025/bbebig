package com.smilegate.bbebig.presentation.ui.signup.phonenumber.mvi

import com.smilegate.bbebig.presentation.base.UiState

data class InputPhoneNumberUiState(
    val isLoading: Boolean,
    val phoneNumber: String,
) : UiState {
    companion object {
        fun initial() = InputPhoneNumberUiState(
            phoneNumber = "",
            isLoading = false,
        )
    }
}
