package com.smilegate.bbebig.presentation.ui.home

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.smilegate.bbebig.domain.usecase.ConnectChatUseCase
import com.smilegate.bbebig.domain.usecase.DisconnectChatUseCase
import com.smilegate.bbebig.domain.usecase.GetHistoryChatListUseCase
import com.smilegate.bbebig.domain.usecase.GetMyInfoUseCase
import com.smilegate.bbebig.domain.usecase.GetServerInfoUseCase
import com.smilegate.bbebig.domain.usecase.GetServerListUseCase
import com.smilegate.bbebig.domain.usecase.GetUserInfoUseCase
import com.smilegate.bbebig.domain.usecase.ReceiveChatUseCase
import com.smilegate.bbebig.domain.usecase.SendChatUseCase
import com.smilegate.bbebig.presentation.base.BaseViewModel
import com.smilegate.bbebig.presentation.model.ChatType
import com.smilegate.bbebig.presentation.model.MessageWriteType
import com.smilegate.bbebig.presentation.ui.home.model.ChannelInfo
import com.smilegate.bbebig.presentation.ui.home.model.ChatModel
import com.smilegate.bbebig.presentation.ui.home.model.ChatRoomType
import com.smilegate.bbebig.presentation.ui.home.model.MemberInfo
import com.smilegate.bbebig.presentation.ui.home.model.MessageContent
import com.smilegate.bbebig.presentation.ui.home.model.Server
import com.smilegate.bbebig.presentation.ui.home.model.ServerInfo
import com.smilegate.bbebig.presentation.ui.home.model.toDomainModel
import com.smilegate.bbebig.presentation.ui.home.model.toUiModel
import com.smilegate.bbebig.presentation.ui.home.mvi.HomeIntent
import com.smilegate.bbebig.presentation.ui.home.mvi.HomeSideEffect
import com.smilegate.bbebig.presentation.ui.home.mvi.HomeUiState
import com.smilegate.bbebig.presentation.utils.ImmutableList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getServerListUseCase: GetServerListUseCase,
    private val getServerInfoUseCase: GetServerInfoUseCase,
    private val connectChatUseCase: ConnectChatUseCase,
    private val disconnectChatUseCase: DisconnectChatUseCase,
    private val sendChatUseCase: SendChatUseCase,
    private val receiveChatUseCase: ReceiveChatUseCase,
    private val getMyInfoUseCase: GetMyInfoUseCase,
    private val getHistoryChatListUseCase: GetHistoryChatListUseCase,
    private val getUserInfoUseCase: GetUserInfoUseCase,
) : BaseViewModel<HomeUiState, HomeSideEffect, HomeIntent>(savedStateHandle) {

    init {
        fetchMyInfo()
        fetchServerList()
    }

    override fun createInitialState(savedStateHandle: SavedStateHandle): HomeUiState {
        return HomeUiState.initialize()
    }

    override fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.ClickServer -> {
                fetchServerInfo(serverId = intent.serverId)
                connectChatServer()
            }

            is HomeIntent.ClickChannelRoom -> {
                updateSelectedChannelInfo(intent.channelInfo)
                when (ChatRoomType.matchType(intent.channelInfo.channelType)) {
                    ChatRoomType.VOICE -> {
                        updateJoinBottomSheet(isVisible = true)
                    }

                    ChatRoomType.CHAT -> {
                        updateLoadingState(isLoading = true)
                        fetchHistoryChat()
                        receiveChatMessage(
                            serverId = uiState.value.serverInfo.serverId,
                            memberId = uiState.value.myInfo.userId,
                        )
                        updateChatRoomState(isVisible = true)
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
                updateChatRoomState(isVisible = false)
            }

            HomeIntent.DisMissSheet -> {
                updateJoinBottomSheet(isVisible = false)
            }

            HomeIntent.ClickBack -> {
                postSideEffect(HomeSideEffect.NavigateToBack)
            }

            is HomeIntent.ClickSendChat -> {
                sendChannelChat(
                    message = intent.message,
                    messageType = intent.messageType.name,
                )
            }
        }
    }

    private fun updateLoadingState(isLoading: Boolean) {
        reduce {
            copy(
                isLoading = isLoading,
            )
        }
    }

    private fun fetchHistoryChat() {
        viewModelScope.launch {
            getHistoryChatListUseCase(
                serverId = uiState.value.serverInfo.serverId,
                channelId = uiState.value.selectedChannelInfo.channelId,
            ).onSuccess {
                updateMessageList(it.toUiModel())
                updateLoadingState(isLoading = false)
            }.onFailure {
                Log.d("HomeViewModel", "history fail: ${it.message}")
                updateLoadingState(isLoading = false)
            }
        }
    }

    private fun updateMessageList(messages: List<MessageContent>) {
        reduce {
            copy(
                receiveChatMessageList = ImmutableList(messages),
            )
        }
    }

    private fun fetchMyInfo() {
        viewModelScope.launch {
            getMyInfoUseCase()
                .onSuccess {
                    updateMyInfo(it.toUiModel())
                }
                .onFailure {
                    updateErrorSate(isError = true)
                }
        }
    }

    private fun updateMyInfo(memberInfo: MemberInfo) {
        reduce {
            copy(
                myInfo = memberInfo,
            )
        }
    }

    private fun connectChatServer() {
        viewModelScope.launch {
            connectChatUseCase(
                roomType = ChatType.CHAT.name,
                channelId = uiState.value.selectedChannelInfo.channelId,
                serverId = uiState.value.serverId,
                memberId = uiState.value.myInfo.userId,
            ).onSuccess {
                Log.d("HomeViewModel", "connect Success$it")
            }.onFailure {
                Log.d("HomeViewModel", "connect fail: ${it.message}")
            }
            updateChatServerState(isConnected = true)
        }
    }

    private fun updateChatServerState(isConnected: Boolean) {
        reduce {
            copy(
                isChatServerConnected = isConnected,
            )
        }
    }

    private fun updateChatRoomState(isVisible: Boolean) {
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

    private fun sendChannelChat(message: String, messageType: String) {
        viewModelScope.launch {
            sendChatUseCase(
                memberId = uiState.value.myInfo.userId,
                chatModel = ChatModel(
                    chatType = ChatType.CHANNEL.name,
                    content = message,
                    serverId = uiState.value.serverInfo.serverId,
                    channelId = uiState.value.selectedChannelInfo.channelId,
                    sendMemberId = uiState.value.myInfo.userId,
                    type = MessageWriteType.MESSAGE_CREATE.name,
                    messageType = messageType,
                    createdAt = null,
                ).toDomainModel(),
            )
        }
    }

    private fun receiveChatMessage(serverId: Long, memberId: Long) {
        viewModelScope.launch {
            receiveChatUseCase(serverId, memberId)
                .map { it.toUiModel() }
                .catch {
                    updateErrorSate(isError = true)
                }.collect {
                    if (uiState.value.selectedChannelInfo.channelId == it.channelId) {
                        updateReceiveChatMessage(it)
                    }
                }
        }
    }

    private fun updateReceiveChatMessage(message: ChatModel) {
        viewModelScope.launch {
            val list = uiState.value
                .receiveChatMessageList
                .toMutableList()
                .also { chatMessageList ->
                    fetchUserInfo(message)
                        ?.let { messageContent ->
                            chatMessageList.add(messageContent)
                        }
                        ?: run {
                            updateErrorSate(isError = true)
                            return@launch
                        }
                }
                .toList()
                .reversed()
            reduce {
                copy(
                    receiveChatMessageList = ImmutableList(list),
                )
            }
        }
    }

    private suspend fun fetchUserInfo(message: ChatModel): MessageContent? {
        return getUserInfoUseCase(memberId = message.sendMemberId).map {
            MessageContent.toMessage(
                it.toUiModel(),
                message,
            )
        }.getOrNull()
    }

    private fun fetchServerList() {
        viewModelScope.launch {
            getServerListUseCase()
                .onSuccess { serverList ->
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
