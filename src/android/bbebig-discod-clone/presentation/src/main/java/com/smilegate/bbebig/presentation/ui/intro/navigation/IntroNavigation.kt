package com.smilegate.bbebig.presentation.ui.intro.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smilegate.bbebig.presentation.navigation.UserRoute

fun NavController.navigateToIntro() {
    navigate(
        route = UserRoute.Intro.route,
    )
}

fun NavGraphBuilder.introNavigation(
    navigateToLogin: () -> Unit,
    navigateToSignUp: () -> Unit,
) {
    composable(
        route = UserRoute.Intro.route,
    ) {
        IntroRoute(
            navigateToLogin = navigateToLogin,
            navigateToSignUp = navigateToSignUp,
        )
    }
}
