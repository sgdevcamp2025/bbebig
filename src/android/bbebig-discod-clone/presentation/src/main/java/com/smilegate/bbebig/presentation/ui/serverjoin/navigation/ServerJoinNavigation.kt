package com.smilegate.bbebig.presentation.ui.serverjoin.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smilegate.bbebig.presentation.navigation.SerVerJoin
import com.smilegate.bbebig.presentation.ui.serverjoin.ServerJoinScreen

fun NavController.navigateToServerJoin() {
    navigate(
        route = SerVerJoin,
    )
}

fun NavGraphBuilder.serverJoinNavigation(
    onBackClick: () -> Unit,
) {
    composable<SerVerJoin> {
        ServerJoinScreen(
            onClickBack = {},
            onClickServerJoin = {},
        )
    }
}
