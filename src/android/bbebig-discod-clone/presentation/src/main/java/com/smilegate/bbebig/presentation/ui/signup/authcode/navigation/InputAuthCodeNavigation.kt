package com.smilegate.bbebig.presentation.ui.signup.authcode.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smilegate.bbebig.presentation.navigation.signup.InputAuthCode
import com.smilegate.bbebig.presentation.navigation.signup.SignUp

fun NavController.navigateToAuthCode() {
    navigate(
        route = InputAuthCode,
    )
}

fun NavGraphBuilder.authCodeNavigation(
    navController: NavController,
    onBackClick: () -> Unit,
    navigateToNickname: () -> Unit,
) {
    composable<InputAuthCode> {
        AuthCodeRoute(
            onBackClick = onBackClick,
            navigateToNickname = navigateToNickname,
            navBackStackEntry = navController.getBackStackEntry<SignUp>(),
        )
    }
}
