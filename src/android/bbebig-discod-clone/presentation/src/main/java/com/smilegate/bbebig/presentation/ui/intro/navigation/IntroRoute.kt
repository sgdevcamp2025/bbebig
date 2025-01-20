package com.smilegate.bbebig.presentation.ui.intro.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smilegate.bbebig.presentation.ui.intro.IntroScreen
import com.smilegate.bbebig.presentation.ui.intro.IntroViewModel
import com.smilegate.bbebig.presentation.ui.intro.mvi.IntroIntent
import com.smilegate.bbebig.presentation.ui.intro.mvi.IntroSideEffect

@Composable
fun IntroRoute(
    modifier: Modifier = Modifier,
    navigateToLogin: () -> Unit,
    navigateToSignUp: () -> Unit,
    viewModel: IntroViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    IntroScreen(
        modifier = modifier,
        onNavigateToLoginClick = { viewModel.handleIntent(IntroIntent.ClickLogin) },
        onNavigateToSignUpClick = { viewModel.handleIntent(IntroIntent.ClickSignUp) },
    )

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is IntroSideEffect.NavigateToLogin -> navigateToLogin()
                is IntroSideEffect.NavigateToSignUp -> navigateToSignUp()
            }
        }
    }
}
