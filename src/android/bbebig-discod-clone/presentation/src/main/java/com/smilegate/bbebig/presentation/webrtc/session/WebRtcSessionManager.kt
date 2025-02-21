package com.smilegate.bbebig.presentation.webrtc.session

import com.smilegate.bbebig.presentation.webrtc.peer.StreamPeerConnectionFactory
import kotlinx.coroutines.flow.SharedFlow
import org.webrtc.IceCandidate
import org.webrtc.SessionDescription
import org.webrtc.VideoTrack

interface WebRtcSessionManager {
    val sdp: SharedFlow<SessionDescription>
    val iceCandidate: SharedFlow<IceCandidate>
    val peerConnectionFactory: StreamPeerConnectionFactory

    val localVideoTrackFlow: SharedFlow<VideoTrack>

    val remoteVideoTrackFlow: SharedFlow<VideoTrack>

    fun onSessionScreenReady()

    fun flipCamera()

    fun enableMicrophone(enabled: Boolean)

    fun enableCamera(enabled: Boolean)

    fun disconnect()
}
