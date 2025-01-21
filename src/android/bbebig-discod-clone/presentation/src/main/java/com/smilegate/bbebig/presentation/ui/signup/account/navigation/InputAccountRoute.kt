package com.smilegate.bbebig.presentation.ui.signup.account.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.smilegate.bbebig.presentation.ui.signup.SignUpViewModel
import com.smilegate.bbebig.presentation.ui.signup.account.AccountScreen
import com.smilegate.bbebig.presentation.ui.signup.account.InputAccountViewModel
import com.smilegate.bbebig.presentation.ui.signup.account.mvi.InputAccountIntent
import com.smilegate.bbebig.presentation.ui.signup.account.mvi.InputAccountSideEffect
import com.smilegate.bbebig.presentation.ui.signup.mvi.SignUpIntent

@Composable
fun AccountRoute(
    navBackStackEntry: NavBackStackEntry,
    navigateToPhoneNumber: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: InputAccountViewModel = hiltViewModel(),
    sharedViewModel: SignUpViewModel = hiltViewModel(navBackStackEntry),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AccountScreen(
        uiState = uiState,
        onBackClick = { viewModel.handleIntent(InputAccountIntent.ClickBack) },
        onClickConfirm = { userName, password ->
            viewModel.handleIntent(InputAccountIntent.ClickConfirm(userName, password))
        },
    )

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is InputAccountSideEffect.NavigatePhoneNumber -> {
                    sharedViewModel.handleIntent(SignUpIntent.ConfirmAccount(uiState.userName, uiState.password))
                    navigateToPhoneNumber()
                }
                is InputAccountSideEffect.NavigateToBack -> onBackClick()
            }
        }
    }
}
