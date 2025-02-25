package com.smilegate.bbebig.presentation.ui.splash.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.smilegate.bbebig.presentation.navigation.Splash
import com.smilegate.bbebig.presentation.ui.home.navigation.navigateToHome
import com.smilegate.bbebig.presentation.ui.intro.navigation.navigateToIntro

fun NavController.navigateToSplash() {
    navigate(
        route = Splash,
    ) {
        popUpTo(Splash) {
            inclusive = true
        }
    }
}

fun NavGraphBuilder.splashNavigation(navController: NavHostController) {
    composable<Splash> {
        SplashRoute(
            onNavigateToIntro = navController::navigateToIntro,
            onNavigateToHome = navController::navigateToHome,
        )
    }
}
