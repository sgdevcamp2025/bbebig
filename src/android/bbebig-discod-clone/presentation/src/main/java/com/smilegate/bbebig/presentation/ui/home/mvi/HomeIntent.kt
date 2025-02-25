package com.smilegate.bbebig.presentation.ui.home.mvi

import com.smilegate.bbebig.presentation.base.UiIntent
import com.smilegate.bbebig.presentation.ui.home.model.ChannelInfo

sealed interface HomeIntent : UiIntent {
    data class ClickServer(val serverId: Long) : HomeIntent
    data object ClickCreateServer : HomeIntent
    data object ClickJoinServer : HomeIntent
    data object ClickInviteFriend : HomeIntent
    data object ClickSearchChat : HomeIntent
    data object ClickChannelTitle : HomeIntent
    data class ClickChannelRoom(val channelInfo: ChannelInfo) : HomeIntent
    data class ClickLiveChatRoom(val channelId: Long) : HomeIntent
    data object ClickBackChatRoom : HomeIntent
    data object DisMissSheet : HomeIntent
    data object ClickBack : HomeIntent
}
