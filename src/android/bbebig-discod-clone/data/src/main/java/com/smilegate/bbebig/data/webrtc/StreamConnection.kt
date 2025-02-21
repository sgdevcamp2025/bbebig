package com.smilegate.bbebig.data.webrtc

import com.smilegate.bbebig.data.utils.addRtcIceCandidate
import com.smilegate.bbebig.data.utils.createValue
import com.smilegate.bbebig.data.utils.setValue
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.webrtc.DataChannel
import org.webrtc.IceCandidate
import org.webrtc.MediaConstraints
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.RtpReceiver
import org.webrtc.RtpTransceiver
import org.webrtc.SessionDescription
import timber.log.Timber

class StreamConnection(
    private val onStreamAdded: ((MediaStream) -> Unit)?,
    private val onNegotiationNeeded: ((StreamConnection) -> Unit)?,
    private val onIceCandidate: (IceCandidate) -> Unit?,
    private val onVideoTrack: ((RtpTransceiver?) -> Unit)?,
    private val mediaConstraints: MediaConstraints,
    ) : PeerConnection.Observer {

    lateinit var connection: PeerConnection
        private set

    private val pendingIceMutex = Mutex()
    private val pendingIceCandidates = mutableListOf<IceCandidate>()

    fun initialize(peerConnection: PeerConnection) {
        Timber.d("[initialize] #sfu; peerConnection: $peerConnection")
        this.connection = peerConnection
    }

    suspend fun createOffer(): Result<SessionDescription> {
        Timber.d("[createOffer] #sfu; no args")
        return createValue { connection.createOffer(it, mediaConstraints) }
    }


    suspend fun createAnswer(): Result<SessionDescription> {
        Timber.d("[createAnswer] #sfu; no args")
        return createValue { connection.createAnswer(it, mediaConstraints) }
    }

    suspend fun setRemoteDescription(sessionDescription: SessionDescription): Result<Unit> {
        Timber.d("[setRemoteDescription] #sfu; answerSdp: ${sessionDescription}")
        return setValue {
            connection.setRemoteDescription(
                it,
                SessionDescription(
                    sessionDescription.type,
                    sessionDescription.description.mungeCodecs(),
                ),
            )
        }.also {
            pendingIceMutex.withLock {
                pendingIceCandidates.forEach { iceCandidate ->
                    Timber.d("[setRemoteDescription] #sfu; #subscriber; pendingRtcIceCandidate: $iceCandidate")
                    connection.addRtcIceCandidate(iceCandidate)
                }
                pendingIceCandidates.clear()
            }
        }
    }

    suspend fun setLocalDescription(sessionDescription: SessionDescription): Result<Unit> {
        val sdp = SessionDescription(
            sessionDescription.type,
            sessionDescription.description.mungeCodecs(),
        )
        Timber.d("[setLocalDescription] #sfu; offerSdp: ${sessionDescription}")
        return setValue { connection.setLocalDescription(it, sdp) }
    }

    suspend fun addIceCandidate(iceCandidate: IceCandidate): Result<Unit> {
        if (connection.remoteDescription == null) {
            Timber.w ( "[addIceCandidate] #sfu; postponed (no remoteDescription): $iceCandidate" )
            pendingIceMutex.withLock {
                pendingIceCandidates.add(iceCandidate)
            }
            return Result.failure(RuntimeException("RemoteDescription is not set"))
        }
        Timber.d ( "[addIceCandidate] #sfu; rtcIceCandidate: $iceCandidate" )
        return connection.addRtcIceCandidate(iceCandidate).also {
            Timber.v ( "[addIceCandidate] #sfu; completed: $it" )
        }
    }

    override fun onSignalingChange(p0: PeerConnection.SignalingState?) {
    }

    override fun onIceConnectionChange(newState: PeerConnection.IceConnectionState?) {
    }

    override fun onIceConnectionReceivingChange(p0: Boolean) {
    }

    override fun onIceGatheringChange(p0: PeerConnection.IceGatheringState?) {
    }

    override fun onIceCandidate(candidate: IceCandidate?) {
        candidate?.let {
            onIceCandidate.invoke(it)
        }
    }

    override fun onIceCandidatesRemoved(p0: Array<out IceCandidate>?) {
    }

    override fun onAddStream(stream: MediaStream?) {
        stream?.let {
            onStreamAdded?.invoke(it)
        }
    }

    override fun onTrack(transceiver: RtpTransceiver?) {
        onVideoTrack?.invoke(transceiver)
    }

    override fun onAddTrack(receiver: RtpReceiver?, mediaStreams: Array<out MediaStream>?) {
        mediaStreams?.forEach { mediaStream ->
            mediaStream.audioTracks?.forEach { remoteAudioTrack ->
                remoteAudioTrack.setEnabled(true)
            }
            onStreamAdded?.invoke(mediaStream)
        }
    }

    override fun onRemoveStream(p0: MediaStream?) {
    }

    override fun onDataChannel(p0: DataChannel?) {
    }

    override fun onRenegotiationNeeded() {
        onNegotiationNeeded?.invoke(this@StreamConnection)
    }
    private fun String.mungeCodecs(): String {
        return this.replace("vp9", "VP9").replace("vp8", "VP8").replace("h264", "H264")
    }
}