package com.smilegate.bbebig.presentation.ui.login.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smilegate.bbebig.presentation.navigation.user.Login

fun NavController.navigateToLogin() {
    navigate(
        route = Login,
    )
}

fun NavGraphBuilder.loginNavigation(
    navigateToHome: () -> Unit,
    onBackClick: () -> Unit,
) {
    composable<Login> {
        LoginRoute(
            navigateToHome = navigateToHome,
            onBackClick = onBackClick,
        )
    }
}
