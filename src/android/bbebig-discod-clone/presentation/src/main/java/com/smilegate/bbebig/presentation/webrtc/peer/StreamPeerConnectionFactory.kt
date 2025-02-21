package com.smilegate.bbebig.presentation.webrtc.peer

import android.content.Context
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import org.webrtc.AudioSource
import org.webrtc.AudioTrack
import org.webrtc.DefaultVideoDecoderFactory
import org.webrtc.EglBase
import org.webrtc.HardwareVideoEncoderFactory
import org.webrtc.IceCandidate
import org.webrtc.Logging
import org.webrtc.MediaConstraints
import org.webrtc.MediaStream
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import org.webrtc.RtpTransceiver
import org.webrtc.SimulcastVideoEncoderFactory
import org.webrtc.SoftwareVideoEncoderFactory
import org.webrtc.VideoSource
import org.webrtc.VideoTrack
import org.webrtc.audio.JavaAudioDeviceModule
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StreamPeerConnectionFactory @Inject constructor(
    @ApplicationContext context: Context,
) {
    val eglBaseContext: EglBase.Context by lazy {
        EglBase.create().eglBaseContext
    }

    private val videoDecoderFactory by lazy {
        DefaultVideoDecoderFactory(
            eglBaseContext,
        )
    }

    val rtcConfig = PeerConnection.RTCConfiguration(
        arrayListOf(
            PeerConnection.IceServer.builder("stun:stun.l.google.com:19302").createIceServer(),
            PeerConnection.IceServer.builder("stun:stun1.l.google.com:19302").createIceServer(),
            PeerConnection.IceServer.builder("stun:stun2.l.google.com:19302").createIceServer(),
            PeerConnection.IceServer.builder("stun:stun3.l.google.com:19302").createIceServer(),
            PeerConnection.IceServer.builder("stun:stun4.l.google.com:19302").createIceServer(),
        ),
    ).apply {
        sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN
    }

    private val videoEncoderFactory by lazy {
        val hardwareEncoder = HardwareVideoEncoderFactory(eglBaseContext, true, true)
        SimulcastVideoEncoderFactory(hardwareEncoder, SoftwareVideoEncoderFactory())
    }

    private val factory by lazy {
        PeerConnectionFactory.initialize(
            PeerConnectionFactory
                .InitializationOptions
                .builder(context)
                .createInitializationOptions(),
        )

        PeerConnectionFactory.builder()
            .setVideoDecoderFactory(videoDecoderFactory)
            .setVideoEncoderFactory(videoEncoderFactory)
            .setAudioDeviceModule(
                JavaAudioDeviceModule
                    .builder(context)
                    .setUseHardwareAcousticEchoCanceler(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    .setUseHardwareNoiseSuppressor(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    .setAudioRecordErrorCallback(
                        object : JavaAudioDeviceModule.AudioRecordErrorCallback {
                            override fun onWebRtcAudioRecordInitError(p0: String?) {
                            }

                            override fun onWebRtcAudioRecordStartError(
                                p0: JavaAudioDeviceModule.AudioRecordStartErrorCode?,
                                p1: String?,
                            ) {
                            }

                            override fun onWebRtcAudioRecordError(p0: String?) {
                            }
                        },
                    )
                    .setAudioTrackErrorCallback(
                        object :
                            JavaAudioDeviceModule.AudioTrackErrorCallback {
                            override fun onWebRtcAudioTrackInitError(p0: String?) {
                            }

                            override fun onWebRtcAudioTrackStartError(
                                p0: JavaAudioDeviceModule.AudioTrackStartErrorCode?,
                                p1: String?,
                            ) {
                            }

                            override fun onWebRtcAudioTrackError(p0: String?) {
                            }
                        },
                    )
                    .setAudioRecordStateCallback(
                        object :
                            JavaAudioDeviceModule.AudioRecordStateCallback {
                            override fun onWebRtcAudioRecordStart() {
                            }

                            override fun onWebRtcAudioRecordStop() {
                            }
                        },
                    )
                    .setAudioTrackStateCallback(
                        object :
                            JavaAudioDeviceModule.AudioTrackStateCallback {
                            override fun onWebRtcAudioTrackStart() {
                            }

                            override fun onWebRtcAudioTrackStop() {
                            }
                        },
                    )
                    .createAudioDeviceModule().also {
                        it.setMicrophoneMute(false)
                        it.setSpeakerMute(false)
                    },
            )
            .createPeerConnectionFactory()
    }

    fun makePeerConnection(
        coroutineScope: CoroutineScope,
        configuration: PeerConnection.RTCConfiguration,
        mediaConstraints: MediaConstraints,
        onStreamAdded: ((MediaStream) -> Unit)? = null,
        onNegotiationNeeded: ((StreamPeerConnection, StreamPeerType) -> Unit)? = null,
        onIceCandidateRequest: ((IceCandidate, String) -> Unit)? = null,
        onVideoTrack: ((RtpTransceiver?) -> Unit)? = null,
    ): StreamPeerConnection {
        val peerConnection = StreamPeerConnection(
            coroutineScope = coroutineScope,
            mediaConstraints = mediaConstraints,
            onStreamAdded = onStreamAdded,
            onNegotiationNeeded = onNegotiationNeeded,
            onIceCandidate = onIceCandidateRequest,
            onVideoTrack = onVideoTrack,
        )
        val connection = makePeerConnectionInternal(
            configuration = configuration,
            observer = peerConnection,
        )
        return peerConnection.apply { initialize(connection) }
    }

    private fun makePeerConnectionInternal(
        configuration: PeerConnection.RTCConfiguration,
        observer: PeerConnection.Observer?,
    ): PeerConnection {
        return requireNotNull(
            factory.createPeerConnection(
                configuration,
                observer,
            ),
        )
    }

    /**
     * Builds a [VideoSource] from the [factory] that can be used for regular video share (camera)
     * or screen sharing.
     *
     * @param isScreencast If we're screen sharing using this source.
     * @return [VideoSource] that can be used to build tracks.
     */
    fun makeVideoSource(isScreencast: Boolean): VideoSource =
        factory.createVideoSource(isScreencast)

    /**
     * Builds a [VideoTrack] from the [factory] that can be used for regular video share (camera)
     * or screen sharing.
     *
     * @param source The [VideoSource] used for the track.
     * @param trackId The unique ID for this track.
     * @return [VideoTrack] That represents a video feed.
     */
    fun makeVideoTrack(
        source: VideoSource,
        trackId: String,
    ): VideoTrack = factory.createVideoTrack(trackId, source)

    /**
     * Builds an [AudioSource] from the [factory] that can be used for audio sharing.
     *
     * @param constraints The constraints used to change the way the audio behaves.
     * @return [AudioSource] that can be used to build tracks.
     */
    fun makeAudioSource(constraints: MediaConstraints = MediaConstraints()): AudioSource =
        factory.createAudioSource(constraints)

    /**
     * Builds an [AudioTrack] from the [factory] that can be used for regular video share (camera)
     * or screen sharing.
     *
     * @param source The [AudioSource] used for the track.
     * @param trackId The unique ID for this track.
     * @return [AudioTrack] That represents an audio feed.
     */
    fun makeAudioTrack(
        source: AudioSource,
        trackId: String,
    ): AudioTrack = factory.createAudioTrack(trackId, source)
}
