//package com.smilegate.bbebig.data.webrtc
//
//import android.content.Context
//import android.os.Build
//import dagger.hilt.android.qualifiers.ApplicationContext
//import kotlinx.coroutines.CoroutineScope
//import org.webrtc.AudioSource
//import org.webrtc.AudioTrack
//import org.webrtc.DefaultVideoDecoderFactory
//import org.webrtc.EglBase
//import org.webrtc.HardwareVideoEncoderFactory
//import org.webrtc.IceCandidate
//import org.webrtc.MediaConstraints
//import org.webrtc.MediaStream
//import org.webrtc.PeerConnection
//import org.webrtc.PeerConnectionFactory
//import org.webrtc.RtpTransceiver
//import org.webrtc.SimulcastVideoEncoderFactory
//import org.webrtc.SoftwareVideoEncoderFactory
//import org.webrtc.VideoSource
//import org.webrtc.VideoTrack
//import org.webrtc.audio.JavaAudioDeviceModule
//import javax.inject.Inject
//
//class PeerConnectionFactory @Inject constructor(
//    @ApplicationContext context: Context,
//) {
//
////    private val videoEncoderFactory by lazy {
////        val hardwareEncoder = HardwareVideoEncoderFactory(eglBaseContext, true, true)
////        SimulcastVideoEncoderFactory(hardwareEncoder, SoftwareVideoEncoderFactory())
////    }
//
////    private val factory: PeerConnectionFactory by lazy {
////        PeerConnectionFactory.initialize(
////            PeerConnectionFactory
////                .InitializationOptions
////                .builder(context)
////                .createInitializationOptions(),
////        )
////
////        PeerConnectionFactory.builder()
////            .setVideoDecoderFactory(videoDecoderFactory)
////            .setVideoEncoderFactory(videoEncoderFactory)
////            .setAudioDeviceModule(
////                JavaAudioDeviceModule
////                    .builder(context)
////                    .setUseHardwareAcousticEchoCanceler(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
////                    .setUseHardwareNoiseSuppressor(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
////                    .setAudioRecordErrorCallback(AudioDeviceErrorCallBack)
////                    .setAudioTrackErrorCallback(AudioDeviceErrorCallBack)
////                    .setAudioRecordStateCallback(AudioDeviceErrorCallBack)
////                    .setAudioTrackStateCallback(AudioDeviceErrorCallBack)
////                    .createAudioDeviceModule().also {
////                        it.setMicrophoneMute(false)
////                        it.setSpeakerMute(false)
////                    },
////            )
////            .createPeerConnectionFactory()
////    }
//
//    fun makePeerConnection(
//        coroutineScope: CoroutineScope,
//        configuration: PeerConnection.RTCConfiguration,
//        mediaConstraints: MediaConstraints,
//        onStreamAdded: ((MediaStream) -> Unit)? = null,
//        onNegotiationNeeded: ((StreamPeerConnection, StreamPeerType) -> Unit)? = null,
//        onIceCandidateRequest: ((IceCandidate, String) -> Unit)? = null,
//        onVideoTrack: ((RtpTransceiver?) -> Unit)? = null,
//    ): StreamPeerConnection {
//        val peerConnection = StreamPeerConnection(
//            coroutineScope = coroutineScope,
//            mediaConstraints = mediaConstraints,
//            onStreamAdded = onStreamAdded,
//            onNegotiationNeeded = onNegotiationNeeded,
//            onIceCandidate = onIceCandidateRequest,
//            onVideoTrack = onVideoTrack,
//        )
//        val connection = makePeerConnectionInternal(
//            configuration = configuration,
//            observer = peerConnection,
//        )
//        return peerConnection.apply { initialize(connection) }
//    }
//
//    private fun makePeerConnectionInternal(
//        configuration: PeerConnection.RTCConfiguration,
//        observer: PeerConnection.Observer?,
//    ): PeerConnection {
//        return requireNotNull(
//            factory.createPeerConnection(
//                configuration,
//                observer,
//            ),
//        )
//    }
//
//    fun makeVideoSource(isScreencast: Boolean): VideoSource =
//        factory.createVideoSource(isScreencast)
//
//    fun makeVideoTrack(
//        source: VideoSource,
//        trackId: String,
//    ): VideoTrack = factory.createVideoTrack(trackId, source)
//
//
//    fun makeAudioSource(constraints: MediaConstraints = MediaConstraints()): AudioSource =
//        factory.createAudioSource(constraints)
//
//    fun makeAudioTrack(
//        source: AudioSource,
//        trackId: String,
//    ): AudioTrack = factory.createAudioTrack(trackId, source)
//}