package com.smilegate.bbebig.data.datasource

import com.smilegate.bbebig.data.webrtc.StreamConnection
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.MediaStream
import org.webrtc.RtpTransceiver
import org.webrtc.SessionDescription

interface WebRTCDataSource {
     fun makeConnection(
        onStreamAdded: ((MediaStream) -> Unit)?,
        onNegotiationNeeded: ((StreamConnection) -> Unit)?= null,
        onIceCandidate: (IceCandidate) -> Unit?,
        onVideoTrack: ((RtpTransceiver?) -> Unit)?,
        mediaConstraints: MediaConstraints,
    ): StreamConnection
}