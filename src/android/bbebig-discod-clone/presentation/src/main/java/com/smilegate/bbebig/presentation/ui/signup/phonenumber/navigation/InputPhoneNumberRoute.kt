package com.smilegate.bbebig.presentation.ui.signup.phonenumber.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.smilegate.bbebig.presentation.ui.signup.SignUpViewModel
import com.smilegate.bbebig.presentation.ui.signup.mvi.SignUpIntent
import com.smilegate.bbebig.presentation.ui.signup.phonenumber.InputPhoneNumberViewModel
import com.smilegate.bbebig.presentation.ui.signup.phonenumber.PhoneNumberScreen
import com.smilegate.bbebig.presentation.ui.signup.phonenumber.mvi.InputPhoneNumberIntent
import com.smilegate.bbebig.presentation.ui.signup.phonenumber.mvi.InputPhoneNumberSideEffect

@Composable
fun PhoneNumberRoute(
    navBackStackEntry: NavBackStackEntry,
    onBackClick: () -> Unit,
    navigateToNickname: () -> Unit,
    sharedViewModel: SignUpViewModel = hiltViewModel(navBackStackEntry),
    viewModel: InputPhoneNumberViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PhoneNumberScreen(
        onBackClick = onBackClick,
        onClickConfirm = { phoneNumber ->
            viewModel.handleIntent(InputPhoneNumberIntent.ClickConfirm(phoneNumber))
        },
    )

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collect {
            when (it) {
                is InputPhoneNumberSideEffect.NavigateToAuthCode -> {
                    sharedViewModel.handleIntent(SignUpIntent.ConfirmPhoneNumber(uiState.phoneNumber))
                    navigateToNickname()
                }

                InputPhoneNumberSideEffect.ShowErrorText -> { /*TODO: 향후 에러 처리 코드 추가*/ }
            }
        }
    }
}
