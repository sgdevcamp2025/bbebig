package com.smilegate.bbebig.data.datasource

import com.smilegate.bbebig.data.utils.createValue
import com.smilegate.bbebig.data.webrtc.StreamConnection
import kotlinx.coroutines.sync.Mutex
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import org.webrtc.RtpTransceiver
import org.webrtc.SessionDescription
import javax.inject.Inject

class WebRTCDataSourceImpl @Inject constructor(
    private val peerConnectionFactory: PeerConnectionFactory,
    private val rtcConfiguration: PeerConnection.RTCConfiguration,
) : WebRTCDataSource {

    override fun makeConnection(
        onStreamAdded: ((MediaStream) -> Unit)?,
        onNegotiationNeeded: ((StreamConnection) -> Unit)?,
        onIceCandidate: (IceCandidate) -> Unit?,
        onVideoTrack: ((RtpTransceiver?) -> Unit)?,
        mediaConstraints: MediaConstraints,
    ): StreamConnection {
        val peerConnection = StreamConnection(
            mediaConstraints = mediaConstraints,
            onStreamAdded = onStreamAdded,
            onNegotiationNeeded = onNegotiationNeeded,
            onIceCandidate = onIceCandidate,
            onVideoTrack = onVideoTrack,
        )
        val connection = peerConnectionFactory.createPeerConnection(
            rtcConfiguration,
            peerConnection,
        )
        return peerConnection.apply {
            if (connection != null) {
                initialize(connection)
            }
        }
    }
}