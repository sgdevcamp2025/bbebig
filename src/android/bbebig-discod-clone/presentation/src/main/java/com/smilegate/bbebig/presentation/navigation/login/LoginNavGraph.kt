package com.smilegate.bbebig.presentation.navigation.login

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.smilegate.bbebig.presentation.navigation.DiscordRoute
import com.smilegate.bbebig.presentation.ui.login.navigation.loginNavigation

fun NavGraphBuilder.loginNavGraph(
    navController: NavController,
) {
    loginNavigation(
        navigateToHome = {
            navController.navigate(DiscordRoute.Home.route)
        },
        onBackClick = {
            navController.popBackStack()
        },
    )
}
