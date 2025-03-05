package com.smilegate.bbebig.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.smilegate.bbebig.presentation.navigation.home.homeNavGraph
import com.smilegate.bbebig.presentation.navigation.signup.signUpNavGraph
import com.smilegate.bbebig.presentation.navigation.user.intro.introNavGraph
import com.smilegate.bbebig.presentation.navigation.user.login.loginNavGraph
import com.smilegate.bbebig.presentation.ui.splash.navigation.splashNavigation

@Composable
fun DiscordNavHost(
    modifier: Modifier,
    navController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Splash,
        exitTransition = { ExitTransition.None },
        enterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
    ) {
        splashNavigation(navController = navController)
        introNavGraph(navController = navController)
        signUpNavGraph(navController = navController)
        loginNavGraph(navController = navController)
        homeNavGraph(navController = navController)
    }
}
