package com.smilegate.bbebig.presentation.ui.signup.account.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smilegate.bbebig.presentation.navigation.signup.InputAccount

fun NavController.navigateToAccount() {
    navigate(
        route = InputAccount,
    )
}

fun NavGraphBuilder.accountNavigation(
    navigateToEmail: () -> Unit,
    onBackClick: () -> Unit,
    navController: NavController,
) {
    composable<InputAccount> {
        AccountRoute(
            navigateToPhoneNumber = navigateToEmail,
            onBackClick = onBackClick,
            navBackStackEntry = navController.getBackStackEntry(navController.graph.id),
        )
    }
}
