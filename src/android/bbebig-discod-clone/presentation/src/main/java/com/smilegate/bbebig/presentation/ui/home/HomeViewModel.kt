package com.smilegate.bbebig.presentation.ui.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.smilegate.bbebig.domain.usecase.GetServerInfoUseCase
import com.smilegate.bbebig.domain.usecase.GetServerListUseCase
import com.smilegate.bbebig.presentation.base.BaseViewModel
import com.smilegate.bbebig.presentation.ui.home.model.ChannelInfo
import com.smilegate.bbebig.presentation.ui.home.model.ChatRoomType
import com.smilegate.bbebig.presentation.ui.home.model.Server
import com.smilegate.bbebig.presentation.ui.home.model.ServerInfo
import com.smilegate.bbebig.presentation.ui.home.model.toUiModel
import com.smilegate.bbebig.presentation.ui.home.mvi.HomeIntent
import com.smilegate.bbebig.presentation.ui.home.mvi.HomeSideEffect
import com.smilegate.bbebig.presentation.ui.home.mvi.HomeUiState
import com.smilegate.bbebig.presentation.utils.ImmutableList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getServerListUseCase: GetServerListUseCase,
    private val getServerInfoUseCase: GetServerInfoUseCase,
) : BaseViewModel<HomeUiState, HomeSideEffect, HomeIntent>(savedStateHandle) {

    init {
        fetchServerList()
    }

    override fun createInitialState(savedStateHandle: SavedStateHandle): HomeUiState {
        return HomeUiState.initialize()
    }

    override fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.ClickServer -> {
                fetchServerInfo(serverId = intent.serverId)
            }

            is HomeIntent.ClickChannelRoom -> {
                updateSelectedChannelInfo(intent.channelInfo)
                when (ChatRoomType.matchType(intent.channelInfo.channelType)) {
                    ChatRoomType.VOICE -> {
                        updateJoinBottomSheet(isVisible = true)
                    }

                    ChatRoomType.CHAT -> {
                        postSideEffect(HomeSideEffect.NavigateToChatRoom)
                    }
                }
            }

            is HomeIntent.ClickChannelTitle -> {
            }

            is HomeIntent.ClickLiveChatRoom -> {
                postSideEffect(HomeSideEffect.NavigateToLiveChat(intent.channelId))
            }

            is HomeIntent.ClickCreateServer -> {
            }

            is HomeIntent.ClickInviteFriend -> {
            }

            is HomeIntent.ClickJoinServer -> {
            }

            is HomeIntent.ClickSearchChat -> {
            }

            HomeIntent.ClickBackChatRoom -> {
                updateDrawer(isVisible = false)
            }
        }
    }

    private fun updateDrawer(isVisible: Boolean) {
        reduce {
            copy(
                isChatRoomVisible = isVisible,
            )
        }
    }

    private fun updateSelectedChannelInfo(channelInfo: ChannelInfo) {
        reduce {
            copy(
                selectedChannelInfo = channelInfo,
            )
        }
    }

    private fun updateJoinBottomSheet(isVisible: Boolean) {
        reduce {
            copy(
                isSheetVisible = isVisible,
            )
        }
    }

    private fun fetchServerList() {
        viewModelScope.launch {
            getServerListUseCase()
                .onSuccess { serverList ->
                    Log.d("HomeViewModel", "serverList: $serverList")
                    updateServerList(serverList = serverList.toUiModel())
                }
                .onFailure {
                    updateErrorSate(isError = true)
                }
        }
    }

    private fun fetchServerInfo(serverId: Long) {
        viewModelScope.launch {
            getServerInfoUseCase(serverId)
                .onSuccess { serverInfo ->
                    updateServerInfo(serverInfo = serverInfo.toUiModel())
                }
                .onFailure {
                    updateErrorSate(isError = true)
                }
        }
    }

    private fun updateServerInfo(serverInfo: ServerInfo) {
        reduce {
            copy(serverInfo = serverInfo)
        }
    }

    private fun updateServerList(serverList: List<Server>) {
        reduce {
            copy(serverList = ImmutableList(serverList))
        }
    }

    private fun updateErrorSate(isError: Boolean) {
        reduce {
            copy(isError = isError)
        }
    }
}
