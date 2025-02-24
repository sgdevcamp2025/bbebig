package com.smilegate.bbebig.presentation.ui.signup.email.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smilegate.bbebig.presentation.navigation.signup.InputEmail
import com.smilegate.bbebig.presentation.navigation.user.Intro

fun NavController.navigateToEmail() {
    navigate(
        route = InputEmail,
    )
}

fun NavGraphBuilder.emailNavigation(
    navController: NavController,
    onBackClick: () -> Unit,
    navigateToNickname: () -> Unit,
) {
    composable<InputEmail> {
        EmailRoute(
            onBackClick = onBackClick,
            navigateToNickname = navigateToNickname,
            navBackStackEntry = navController.getBackStackEntry<Intro>(),
        )
    }
}
