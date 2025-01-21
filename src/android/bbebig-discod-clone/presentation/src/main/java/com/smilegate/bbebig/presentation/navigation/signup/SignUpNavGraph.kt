package com.smilegate.bbebig.presentation.navigation.signup

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
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
    navigation<SignUp>(startDestination = InputAccount) {
        accountNavigation(
            navController = navController,
            onBackClick = { navController.popBackStack() },
            navigateToPhoneNumber = { navController.navigateToPhoneNumber() },
        )
        phoneNumberNavigation(
            navController = navController,
            onBackClick = { navController.popBackStack() },
            navigateToNickname = { navController.navigateToNickname() },
        )
        authCodeNavigation(
            navController = navController,
            onBackClick = { navController.popBackStack() },
            navigateToNickname = { navController.navigateToNickname() },
        )
        nicknameNavigation(
            navController = navController,
            onBackClick = { navController.popBackStack() },
            navigateToAge = { navController.navigateToAge() },
        )
        ageNavigation(
            navController = navController,
            onBackClick = { navController.popBackStack() },
            navigateToHome = { /* TODO: home 네비게이션 연결 */ },
        )
    }
}
