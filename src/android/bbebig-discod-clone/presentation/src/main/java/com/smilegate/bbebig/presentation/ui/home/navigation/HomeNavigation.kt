package com.smilegate.bbebig.presentation.ui.home.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smilegate.bbebig.presentation.navigation.Home

fun NavController.navigateToHome() {
    navigate(Home) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
        }
        launchSingleTop = true
    }
}

fun NavGraphBuilder.homeNavigation(
    onNavigateLiveChatRoom: (Long) -> Unit,
    onClickBack: () -> Unit,
) {
    composable<Home> {
        HomeRoute(
            onNavigateLiveChatRoom = onNavigateLiveChatRoom,
            onClickBack = onClickBack,
        )
    }
}
