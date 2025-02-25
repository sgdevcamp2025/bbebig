package com.smilegate.bbebig.presentation.ui.home.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.smilegate.bbebig.presentation.ui.home.HomeScreen
import com.smilegate.bbebig.presentation.ui.home.HomeViewModel
import com.smilegate.bbebig.presentation.ui.home.mvi.HomeIntent
import com.smilegate.bbebig.presentation.ui.home.mvi.HomeSideEffect

@Composable
fun HomeRoute(
    onClickBack: () -> Unit,
    onNavigateLiveChatRoom: (Long) -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(viewModel.sideEffect) {
        viewModel.sideEffect.collect {
            when (it) {
                is HomeSideEffect.NavigateToLiveChat -> {
                    onNavigateLiveChatRoom(it.channelId)
                }

                is HomeSideEffect.NavigateToChatRoom -> {
                }

                HomeSideEffect.NavigateToBack -> {
                    onClickBack()
                }
            }
        }
    }

    BackHandler {
        if (uiState.isChatRoomVisible) {
            viewModel.handleIntent(HomeIntent.ClickBackChatRoom)
        } else {
            viewModel.handleIntent(HomeIntent.ClickBack)
        }
    }

    HomeScreen(
        uiState = uiState,
        onMakeServerClick = {},
        onServerJoinClick = {},
        onClickInviteFriend = {},
        onSearchClick = {},
        onSubChannelClick = {
            viewModel.handleIntent(HomeIntent.ClickChannelRoom(it))
        },
        onClickJoinLiveChat = {
            viewModel.handleIntent(HomeIntent.ClickLiveChatRoom(uiState.selectedChannelInfo.channelId))
        },
        onClickBackChatRoom = {
            viewModel.handleIntent(HomeIntent.ClickBackChatRoom)
        },
        onServerClick = {
            viewModel.handleIntent(HomeIntent.ClickServer(it))
        },
        onDisMissSheet = {
            viewModel.handleIntent(HomeIntent.DisMissSheet)
        },
    )
}
