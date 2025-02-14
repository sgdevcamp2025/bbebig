package com.smilegate.bbebig.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.smilegate.bbebig.presentation.navigation.home.homeNavGraph
import com.smilegate.bbebig.presentation.navigation.signup.signUpNavGraph
import com.smilegate.bbebig.presentation.navigation.user.Intro
import com.smilegate.bbebig.presentation.navigation.user.intro.introNavGraph
import com.smilegate.bbebig.presentation.navigation.user.login.loginNavGraph

@Composable
fun DiscordNavHost(
    modifier: Modifier,
    navController: NavHostController,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = Intro,
        exitTransition = { ExitTransition.None },
        enterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
    ) {
        introNavGraph(navController = navController)
        signUpNavGraph(navController = navController)
        loginNavGraph(navController = navController)
        homeNavGraph(navController = navController)
    }
}
