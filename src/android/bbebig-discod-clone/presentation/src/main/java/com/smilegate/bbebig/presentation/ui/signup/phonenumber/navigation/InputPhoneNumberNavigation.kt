package com.smilegate.bbebig.presentation.ui.signup.phonenumber.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smilegate.bbebig.presentation.navigation.signup.InputPhoneNumber
import com.smilegate.bbebig.presentation.navigation.signup.SignUp

fun NavController.navigateToPhoneNumber() {
    navigate(
        route = InputPhoneNumber,
    )
}

fun NavGraphBuilder.phoneNumberNavigation(
    navController: NavController,
    onBackClick: () -> Unit,
    navigateToNickname: () -> Unit,
) {
    composable<InputPhoneNumber> {
        PhoneNumberRoute(
            onBackClick = onBackClick,
            navigateToNickname = navigateToNickname,
            navBackStackEntry = navController.getBackStackEntry<SignUp>(),
        )
    }
}
