package com.smilegate.bbebig.presentation.ui.signup.nickname.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smilegate.bbebig.presentation.navigation.signup.InputNickname
import com.smilegate.bbebig.presentation.navigation.signup.SignUpNavGraph

fun NavController.navigateToNickname() {
    navigate(
        route = InputNickname,
    )
}

fun NavGraphBuilder.nicknameNavigation(
    navController: NavController,
    onBackClick: () -> Unit,
    navigateToAge: () -> Unit,
) {
    composable<InputNickname> {
        NicknameRoute(
            onBackClick = onBackClick,
            navigateToAge = navigateToAge,
            navBackStackEntry = navController.getBackStackEntry<SignUpNavGraph>(),
        )
    }
}
