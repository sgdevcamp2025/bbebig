package com.smilegate.bbebig.presentation.ui.signup.authcode.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.smilegate.bbebig.presentation.ui.signup.authcode.AuthCodeScreen
import com.smilegate.bbebig.presentation.ui.signup.authcode.InputAuthCodeViewModel
import com.smilegate.bbebig.presentation.ui.signup.authcode.mvi.InputAuthCodeIntent
import com.smilegate.bbebig.presentation.ui.signup.authcode.mvi.InputAuthCodeSideEffect

@Composable
fun AuthCodeRoute(
    navBackStackEntry: NavBackStackEntry,
    onBackClick: () -> Unit,
    navigateToNickname: () -> Unit,
    viewModel: InputAuthCodeViewModel = hiltViewModel(navBackStackEntry),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    AuthCodeScreen(
        onBackClick = onBackClick,
        onClickConfirm = { authCode ->
            viewModel.handleIntent(InputAuthCodeIntent.ClickAuthCodeConfirm(authCode))
        },
    )

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collect {
            when (it) {
                is InputAuthCodeSideEffect.NavigateToNickname -> {
                    navigateToNickname()
                }

                is InputAuthCodeSideEffect.ShowErrorText -> {
                    // TODO: Show error text
                }
            }
        }
    }
}
