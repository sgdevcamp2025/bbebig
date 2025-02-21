package com.smilegate.bbebig.presentation.ui.livechat.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.smilegate.bbebig.presentation.ui.livechat.LiveChatScreen
import com.smilegate.bbebig.presentation.ui.livechat.LiveChatViewModel
import com.smilegate.bbebig.presentation.webrtc.peer.StreamPeerConnectionFactory
import com.smilegate.bbebig.presentation.webrtc.session.WebRtcSessionManager
import com.smilegate.bbebig.presentation.webrtc.session.WebRtcSessionManagerImpl

@Composable
fun LiveChatRoute(
    viewModel: LiveChatViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val sessionManager: WebRtcSessionManager = WebRtcSessionManagerImpl(
        context = context,
        peerConnectionFactory = StreamPeerConnectionFactory(context),
    )
    LaunchedEffect(sessionManager.sdp) {
        sessionManager.sdp.collect {
            viewModel.sendSdp(it)
        }
    }
    LaunchedEffect(sessionManager.iceCandidate) {
        sessionManager.iceCandidate.collect {
            viewModel.sendIceCandidate(it)
        }
    }

    LaunchedEffect(key1 = null) {
        sessionManager.onSessionScreenReady()
    }

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
