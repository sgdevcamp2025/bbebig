package com.smilegate.bbebig.presentation.ui.livechat.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.smilegate.bbebig.presentation.navigation.LiveChat

fun NavController.navigateToLiveChat() {
    navigate(
        route = LiveChat,
    )
}

fun NavGraphBuilder.liveChatNavigation(
    onBackClick: () -> Unit,
) {
    composable<LiveChat> {
        LiveChatRoute()
    }
}
