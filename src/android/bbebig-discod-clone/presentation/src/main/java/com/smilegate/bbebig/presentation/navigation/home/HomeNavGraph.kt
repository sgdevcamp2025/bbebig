package com.smilegate.bbebig.presentation.navigation.home

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.navigation
import com.smilegate.bbebig.presentation.navigation.Home
import com.smilegate.bbebig.presentation.navigation.HomeNavGraph
import com.smilegate.bbebig.presentation.ui.createserver.navigation.createServerNavigation
import com.smilegate.bbebig.presentation.ui.home.navigation.homeNavigation
import com.smilegate.bbebig.presentation.ui.livechat.navigation.liveChatNavigation
import com.smilegate.bbebig.presentation.ui.search.navigation.searchNavigation
import com.smilegate.bbebig.presentation.ui.serverjoin.navigation.serverJoinNavigation

fun NavGraphBuilder.homeNavGraph(
    navController: NavController,
) {
    navigation<HomeNavGraph>(startDestination = Home) {
        homeNavigation(
            onNavigateLiveChatRoom = { channelId ->
                // TODO: 채팅 액티비티로 이동하는 로직
            },
        )
        createServerNavigation()
        liveChatNavigation(
            onBackClick = navController::popBackStack,
        )
        serverJoinNavigation(
            onBackClick = navController::popBackStack,
        )
        searchNavigation(
            onBackClick = navController::popBackStack,
        )
    }
}
