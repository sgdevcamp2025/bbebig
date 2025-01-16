package com.smilegate.bbebig.presentation.ui.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smilegate.bbebig.presentation.navigation.UserRoute

fun NavController.navigateToLogin() {
    navigate(
        route = UserRoute.Login.route,
    )
}

fun NavGraphBuilder.loginNavigation(
    navigateToHome: () -> Unit,
    onBackClick: () -> Unit,
) {
    composable(
        route = UserRoute.Login.route,
    ) {
        LoginRoute(
            navigateToHome = navigateToHome,
            onBackClick = onBackClick,
        )
    }
}
