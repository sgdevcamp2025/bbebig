package com.smilegate.bbebig.presentation.ui.home.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.smilegate.bbebig.presentation.ui.home.HomeScreen
import com.smilegate.bbebig.presentation.ui.home.HomeViewModel

@Composable
fun HomeRoute(
    homeViewModel: HomeViewModel = hiltViewModel(),
) {
    HomeScreen(
        onMakeServerClick = {},
        onServerJoinClick = {},
        onClickInviteFriend = {},
        onSearchClick = {},
    )
}
