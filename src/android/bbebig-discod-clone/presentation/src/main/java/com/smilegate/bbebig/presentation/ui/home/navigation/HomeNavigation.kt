package com.smilegate.bbebig.presentation.ui.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smilegate.bbebig.presentation.navigation.Home

fun NavController.navigateToHome() {
    navigate(route = Home) {
        popUpTo(Home) {
            inclusive = true
        }
    }
}

fun NavGraphBuilder.homeNavigation(onServerJoinClick: () -> Unit) {
    composable<Home> {
        HomeRoute(onServerJoinClick = onServerJoinClick)
    }
}
