package com.smilegate.bbebig.presentation.ui.signup.email.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.smilegate.bbebig.presentation.ui.signup.SignUpViewModel
import com.smilegate.bbebig.presentation.ui.signup.email.InputEmailScreen
import com.smilegate.bbebig.presentation.ui.signup.email.InputEmailViewModel
import com.smilegate.bbebig.presentation.ui.signup.email.mvi.InputEmailIntent
import com.smilegate.bbebig.presentation.ui.signup.email.mvi.InputEmailSideEffect
import com.smilegate.bbebig.presentation.ui.signup.mvi.SignUpIntent

@Composable
fun EmailRoute(
    navBackStackEntry: NavBackStackEntry,
    onBackClick: () -> Unit,
    navigateToNickname: () -> Unit,
    sharedViewModel: SignUpViewModel = hiltViewModel(navBackStackEntry),
    viewModel: InputEmailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    InputEmailScreen(
        onBackClick = onBackClick,
        onClickConfirm = { phoneNumber ->
            viewModel.handleIntent(InputEmailIntent.ClickConfirm(phoneNumber))
        },
    )

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collect {
            when (it) {
                is InputEmailSideEffect.NavigateToAuthCode -> {
                    sharedViewModel.handleIntent(SignUpIntent.ConfirmEmail(uiState.email))
                    navigateToNickname()
                }

                InputEmailSideEffect.ShowErrorText -> { /*TODO: 향후 에러 처리 코드 추가*/ }
            }
        }
    }
}
