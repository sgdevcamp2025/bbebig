package com.smilegate.bbebig.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.smilegate.bbebig.presentation.navigation.intro.introNavGraph
import com.smilegate.bbebig.presentation.navigation.login.loginNavGraph

@Composable
fun UserNavHost(
    modifier: Modifier,
    navController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = UserRoute.Intro.route,
        exitTransition = { ExitTransition.None },
        enterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
    ) {
        introNavGraph(navController = navController)
        loginNavGraph(navController = navController)
    }
}
