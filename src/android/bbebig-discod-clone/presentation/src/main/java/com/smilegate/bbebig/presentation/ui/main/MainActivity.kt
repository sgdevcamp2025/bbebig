package com.smilegate.bbebig.presentation.ui.main

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.smilegate.bbebig.presentation.navigation.DiscordNavHost
import com.smilegate.bbebig.presentation.theme.BbebigTheme
import com.smilegate.bbebig.presentation.webrtc.peer.StreamPeerConnectionFactory
import com.smilegate.bbebig.presentation.webrtc.session.WebRtcSessionManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermissions(arrayOf(Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO), 0)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            BbebigTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DiscordNavHost(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        navController = navController,
                    )


                }
            }
        }
    }
}
