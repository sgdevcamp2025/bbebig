package com.smilegate.bbebig.presentation.ui.livechat.navigation

import androidx.compose.runtime.Composable
import com.smilegate.bbebig.presentation.ui.livechat.LiveChatScreen

@Composable
fun LiveChatRoute() {
    LiveChatScreen(
        title = "Live Chat",
        isVideoChat = true,
        participants = emptyList(),
        onClickPopUp = {},
        onClickMute = {},
        onClickSwitchCamera = {},
        onClickMessageChat = {},
    )
}
