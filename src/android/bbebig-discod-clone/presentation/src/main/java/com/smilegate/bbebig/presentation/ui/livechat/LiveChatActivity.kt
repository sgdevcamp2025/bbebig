package com.smilegate.bbebig.presentation.ui.livechat

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.LaunchedEffect
import com.smilegate.bbebig.presentation.ui.livechat.navigation.LiveChatRoute
import com.smilegate.bbebig.presentation.webrtc.peer.StreamPeerConnectionFactory
import com.smilegate.bbebig.presentation.webrtc.session.WebRtcSessionManager
import com.smilegate.bbebig.presentation.webrtc.session.WebRtcSessionManagerImpl

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LiveChatActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            LiveChatRoute()
        }
    }

    companion object {
        fun openActivity(context: Activity) {
            context.startActivity(
                Intent(context, LiveChatActivity::class.java),
            )
        }
    }
}