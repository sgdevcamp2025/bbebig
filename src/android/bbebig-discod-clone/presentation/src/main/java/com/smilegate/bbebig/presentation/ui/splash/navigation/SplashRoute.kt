package com.smilegate.bbebig.presentation.ui.splash.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.smilegate.bbebig.presentation.ui.splash.SplashScreen
import com.smilegate.bbebig.presentation.ui.splash.SplashViewModel
import com.smilegate.bbebig.presentation.ui.splash.mvi.SplashSideEffect

@Composable
fun SplashRoute(
    onNavigateToIntro: () -> Unit,
    onNavigateToHome: () -> Unit,
    viewModel: SplashViewModel = hiltViewModel(),
) {
    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collect { sideEffect ->
            when (sideEffect) {
                is SplashSideEffect.NavigateToIntro -> {
                    onNavigateToIntro()
                }

                is SplashSideEffect.NavigateToHome -> {
                    onNavigateToHome()
                }
            }
        }
    }
    SplashScreen()
}
