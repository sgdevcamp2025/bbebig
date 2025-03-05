package com.smilegate.bbebig.presentation.ui.signup.age.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smilegate.bbebig.presentation.navigation.signup.InputAge

fun NavController.navigateToAge() {
    navigate(
        route = InputAge,
    )
}

fun NavGraphBuilder.ageNavigation(
    navController: NavController,
    navigateToHome: () -> Unit,
    onBackClick: () -> Unit,
) {
    composable<InputAge> {
        AgeRoute(
            navigateToHome = navigateToHome,
            onBackClick = onBackClick,
            navBackStackEntry = navController.getBackStackEntry(navController.graph.id),
        )
    }
}
