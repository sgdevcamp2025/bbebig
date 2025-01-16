package com.smilegate.bbebig.presentation.navigation.intro

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.smilegate.bbebig.presentation.navigation.UserRoute
import com.smilegate.bbebig.presentation.ui.intro.navigation.introNavigation
import com.smilegate.bbebig.presentation.ui.login.navigation.navigateToLogin

fun NavGraphBuilder.introNavGraph(
    navController: NavController,
) {
    introNavigation(
        navigateToLogin = {
            navController.navigateToLogin()
        },
        navigateToSignUp = {
            navController.navigate(UserRoute.SignUp.route)
        },
    )
}
