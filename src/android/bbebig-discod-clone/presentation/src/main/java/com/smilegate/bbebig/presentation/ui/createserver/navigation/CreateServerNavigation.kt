package com.smilegate.bbebig.presentation.ui.createserver.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smilegate.bbebig.presentation.navigation.CreateServer
import com.smilegate.bbebig.presentation.ui.createserver.CreateServerScreen

fun NavController.navigateToCreateServer() {
    navigate(
        route = CreateServer,
    )
}

fun NavGraphBuilder.createServerNavigation() {
    composable<CreateServer> {
        CreateServerScreen(
            onClickBack = {},
            onClickCreateServer = {},
            onClickAddThumbnail = {},
        )
    }
}
