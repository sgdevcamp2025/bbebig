package com.smilegate.bbebig.presentation.ui.signup.nickname.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import com.smilegate.bbebig.presentation.ui.signup.SignUpViewModel
import com.smilegate.bbebig.presentation.ui.signup.mvi.SignUpIntent
import com.smilegate.bbebig.presentation.ui.signup.nickname.InputNicknameViewModel
import com.smilegate.bbebig.presentation.ui.signup.nickname.NicknameScreen
import com.smilegate.bbebig.presentation.ui.signup.nickname.mvi.InputNicknameIntent
import com.smilegate.bbebig.presentation.ui.signup.nickname.mvi.InputNicknameSideEffect

@Composable
fun NicknameRoute(
    navBackStackEntry: NavBackStackEntry,
    onBackClick: () -> Unit,
    navigateToAge: () -> Unit,
    sharedViewModel: SignUpViewModel = hiltViewModel(navBackStackEntry),
    viewModel: InputNicknameViewModel = hiltViewModel(),
) {
    val uiSate by viewModel.uiState.collectAsStateWithLifecycle()

    NicknameScreen(
        onBackClick = onBackClick,
        onClickConfirm = { nickname ->
            viewModel.handleIntent(InputNicknameIntent.ClickConfirm(nickname))
        },
    )

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collect {
            when (it) {
                InputNicknameSideEffect.NavigateToAge -> {
                    sharedViewModel.handleIntent(SignUpIntent.ConfirmNickname(uiSate.nickname))
                    navigateToAge()
                }

                InputNicknameSideEffect.ShowErrorText -> {
                    // TODO: implement error logic
                }
            }
        }
    }
}
