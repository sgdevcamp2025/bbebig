package com.smilegate.bbebig.presentation.navigation.signup

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.smilegate.bbebig.presentation.ui.home.navigation.navigateToHome
import com.smilegate.bbebig.presentation.ui.intro.navigation.navigateToIntro
import com.smilegate.bbebig.presentation.ui.signup.account.navigation.accountNavigation
import com.smilegate.bbebig.presentation.ui.signup.age.navigation.ageNavigation
import com.smilegate.bbebig.presentation.ui.signup.age.navigation.navigateToAge
import com.smilegate.bbebig.presentation.ui.signup.authcode.navigation.authCodeNavigation
import com.smilegate.bbebig.presentation.ui.signup.nickname.navigation.navigateToNickname
import com.smilegate.bbebig.presentation.ui.signup.nickname.navigation.nicknameNavigation
import com.smilegate.bbebig.presentation.ui.signup.phonenumber.navigation.navigateToPhoneNumber
import com.smilegate.bbebig.presentation.ui.signup.phonenumber.navigation.phoneNumberNavigation

fun NavGraphBuilder.signUpNavGraph(
    navController: NavController,
) {
    navigation<SignUpNavGraph>(startDestination = InputAccount) {
        accountNavigation(
            navController = navController,
            onBackClick = navController::navigateToIntro,
            navigateToPhoneNumber = navController::navigateToPhoneNumber,
        )
        phoneNumberNavigation(
            navController = navController,
            onBackClick = navController::popBackStack,
            navigateToNickname = navController::navigateToNickname,
        )
        authCodeNavigation(
            navController = navController,
            onBackClick = navController::popBackStack,
            navigateToNickname = navController::navigateToNickname,
        )
        nicknameNavigation(
            navController = navController,
            onBackClick = navController::popBackStack,
            navigateToAge = navController::navigateToAge,
        )
        ageNavigation(
            navController = navController,
            onBackClick = navController::popBackStack,
            navigateToHome = navController::navigateToHome,
        )
    }
}
