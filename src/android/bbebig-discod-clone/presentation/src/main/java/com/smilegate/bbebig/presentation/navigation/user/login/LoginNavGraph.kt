package com.smilegate.bbebig.presentation.navigation.user.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.smilegate.bbebig.presentation.ui.home.navigation.navigateToHome
import com.smilegate.bbebig.presentation.ui.login.navigation.loginNavigation

fun NavGraphBuilder.loginNavGraph(
    navController: NavController,
) {
    loginNavigation(
        navigateToHome = navController::navigateToHome,
        onBackClick = navController::popBackStack,
    )
}
