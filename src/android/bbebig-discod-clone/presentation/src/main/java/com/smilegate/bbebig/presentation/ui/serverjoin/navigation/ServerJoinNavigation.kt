package com.smilegate.bbebig.presentation.ui.serverjoin.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smilegate.bbebig.presentation.navigation.ServerJoin
import com.smilegate.bbebig.presentation.ui.serverjoin.ServerJoinScreen

fun NavController.navigateToServerJoin() {
    navigate(
        route = ServerJoin,
    )
}

fun NavGraphBuilder.serverJoinNavigation(
    onBackClick: () -> Unit,
) {
    composable<ServerJoin> {
        ServerJoinScreen(
            onClickBack = {},
            onClickServerJoin = {},
        )
    }
}
