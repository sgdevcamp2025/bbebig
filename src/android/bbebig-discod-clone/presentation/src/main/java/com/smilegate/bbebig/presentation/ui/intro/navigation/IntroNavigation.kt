package com.smilegate.bbebig.presentation.ui.intro.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smilegate.bbebig.presentation.navigation.user.Intro

fun NavController.navigateToIntro() {
    navigate(route = Intro) {
        popUpTo(graph.id) {
            inclusive = true
        }
    }
}

fun NavGraphBuilder.introNavigation(
    navigateToLogin: () -> Unit,
    navigateToSignUp: () -> Unit,
) {
    composable<Intro> {
        IntroRoute(
            navigateToLogin = navigateToLogin,
            navigateToSignUp = navigateToSignUp,
        )
    }
}
