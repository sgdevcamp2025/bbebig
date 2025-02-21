package com.smilegate.bbebig.presentation.ui.livechat.mvi

sealed interface LiveChatIntent {
    data object JoinChannel : LiveChatIntent
    data object LeaveChannel : LiveChatIntent
}