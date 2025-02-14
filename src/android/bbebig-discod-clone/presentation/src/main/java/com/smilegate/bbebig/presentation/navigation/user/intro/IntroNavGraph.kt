package com.smilegate.bbebig.presentation.navigation.user.intro

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.smilegate.bbebig.presentation.navigation.signup.SignUpNavGraph
import com.smilegate.bbebig.presentation.ui.intro.navigation.introNavigation
import com.smilegate.bbebig.presentation.ui.login.navigation.navigateToLogin

fun NavGraphBuilder.introNavGraph(
    navController: NavController,
) {
    introNavigation(
        navigateToLogin = navController::navigateToLogin,
        navigateToSignUp = {
            navController.navigate(SignUpNavGraph) {
                popUpTo<SignUpNavGraph> {
                    inclusive = true
                }
            }
        },
    )
}
