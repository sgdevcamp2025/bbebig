package com.smilegate.bbebig.presentation.navigation.user.intro

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import com.smilegate.bbebig.presentation.navigation.signup.SignUp
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
            navController.navigate(SignUp)
        },
    )
}
