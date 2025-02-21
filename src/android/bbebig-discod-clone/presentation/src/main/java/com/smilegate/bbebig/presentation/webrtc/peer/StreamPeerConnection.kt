package com.smilegate.bbebig.presentation.webrtc.peer

import com.smilegate.bbebig.presentation.ui.livechat.model.SignalMessageType
import com.smilegate.bbebig.data.utils.addRtcIceCandidate
import com.smilegate.bbebig.data.utils.createValue
import com.smilegate.bbebig.data.utils.setValue
import com.smilegate.bbebig.presentation.utils.stringify
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import org.webrtc.CandidatePairChangeEvent
import org.webrtc.DataChannel
import org.webrtc.IceCandidate
import org.webrtc.IceCandidateErrorEvent
import org.webrtc.MediaConstraints
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.RtpReceiver
import org.webrtc.RtpTransceiver
import org.webrtc.SessionDescription
import timber.log.Timber

class StreamPeerConnection(
    private val coroutineScope: CoroutineScope,
    private val mediaConstraints: MediaConstraints,
    private val onStreamAdded: ((MediaStream) -> Unit)?,
    private val onNegotiationNeeded: ((StreamPeerConnection, StreamPeerType) -> Unit)?,
    private val onIceCandidate: ((IceCandidate, String) -> Unit)?,
    private val onVideoTrack: ((RtpTransceiver?) -> Unit)?,
) : PeerConnection.Observer {

    lateinit var connection: PeerConnection
        private set

    private var statsJob: Job? = null

    private val pendingIceMutex = Mutex()
    private val pendingIceCandidates = mutableListOf<IceCandidate>()

    init {
        Timber.d("[StreamPeerConnection] #sfu; #inint")
    }

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
        Timber.d("[setRemoteDescription] #sfu; answerSdp: ${sessionDescription.stringify()}")
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
        Timber.d("[setLocalDescription] #sfu; offerSdp: ${sessionDescription.stringify()}")
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

    override fun onIceCandidate(candidate: IceCandidate?) {
        Timber.i ( "[onIceCandidate] #sfu; candidate: $candidate" )
        if (candidate == null) return

        onIceCandidate?.invoke(candidate, SignalMessageType.OFFER.name)
    }

    override fun onAddStream(stream: MediaStream?) {
        if (stream != null) {
            onStreamAdded?.invoke(stream)
        }
    }


    override fun onAddTrack(receiver: RtpReceiver?, mediaStreams: Array<out MediaStream>?) {
        mediaStreams?.forEach { mediaStream ->
            mediaStream.audioTracks?.forEach { remoteAudioTrack ->
                remoteAudioTrack.setEnabled(true)
            }
            onStreamAdded?.invoke(mediaStream)
        }
    }

    override fun onRenegotiationNeeded() {
        Timber.i ( "[onRenegotiationNeeded] #sfu; no args" )
        onNegotiationNeeded?.invoke(this, StreamPeerType.PUBLISHER)
    }

    override fun onRemoveStream(stream: MediaStream?) {}

    override fun onIceConnectionChange(newState: PeerConnection.IceConnectionState?) {
        Timber.i ( "[onIceConnectionChange] #sfu; ; newState: $newState" )
        when (newState) {
            PeerConnection.IceConnectionState.CLOSED,
            PeerConnection.IceConnectionState.FAILED,
            PeerConnection.IceConnectionState.DISCONNECTED -> statsJob?.cancel()

            PeerConnection.IceConnectionState.CONNECTED -> statsJob = observeStats()
            else -> Unit
        }
    }

    private fun observeStats() = coroutineScope.launch {
        while (isActive) {
            delay(10_000L)
            connection.getStats {
                Timber.v ( "[observeStats] #sfu; stats: $it" )
            }
        }
    }

    override fun onTrack(transceiver: RtpTransceiver?) {
        Timber.i ( "[onTrack] #sfu; transceiver: $transceiver" )
        onVideoTrack?.invoke(transceiver)
    }

    override fun onRemoveTrack(receiver: RtpReceiver?) {
        Timber.d("[onRemoveTrack] #sfu; receiver: $receiver")
    }

    override fun onSignalingChange(newState: PeerConnection.SignalingState?) {
    }

    override fun onIceConnectionReceivingChange(receiving: Boolean) {
    }

    override fun onIceGatheringChange(newState: PeerConnection.IceGatheringState?) {
    }

    override fun onIceCandidatesRemoved(iceCandidates: Array<out org.webrtc.IceCandidate>?) {
    }

    override fun onIceCandidateError(event: IceCandidateErrorEvent?) {
    }

    override fun onConnectionChange(newState: PeerConnection.PeerConnectionState?) {
    }

    override fun onSelectedCandidatePairChanged(event: CandidatePairChangeEvent?) {
    }

    override fun onDataChannel(channel: DataChannel?): Unit = Unit

    override fun toString(): String = "StreamPeerConnection(, constraints=$mediaConstraints)"

    private fun String.mungeCodecs(): String {
        return this.replace("vp9", "VP9").replace("vp8", "VP8").replace("h264", "H264")
    }
}
