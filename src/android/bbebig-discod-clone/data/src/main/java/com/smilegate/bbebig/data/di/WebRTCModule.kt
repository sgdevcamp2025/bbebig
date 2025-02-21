package com.smilegate.bbebig.data.di

import android.content.Context
import android.os.Build
import com.smilegate.bbebig.data.webrtc.AudioDeviceErrorCallBack
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityRetainedScoped
import dagger.hilt.components.SingletonComponent
import org.webrtc.DefaultVideoDecoderFactory
import org.webrtc.EglBase
import org.webrtc.HardwareVideoEncoderFactory
import org.webrtc.PeerConnection
import org.webrtc.PeerConnectionFactory
import org.webrtc.audio.JavaAudioDeviceModule
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class WebRTCModule {

    @Provides
    @Singleton
    fun provideWebRTCConfig(): PeerConnection.RTCConfiguration {
        return PeerConnection.RTCConfiguration(
            arrayListOf(
                PeerConnection.IceServer.builder("stun:stun.l.google.com:19302")
                    .createIceServer(),
                PeerConnection.IceServer.builder("stun:stun1.l.google.com:19302")
                    .createIceServer(),
                PeerConnection.IceServer.builder("stun:stun2.l.google.com:19302")
                    .createIceServer(),
                PeerConnection.IceServer.builder("stun:stun3.l.google.com:19302")
                    .createIceServer(),
                PeerConnection.IceServer.builder("stun:stun4.l.google.com:19302")
                    .createIceServer(),
            ),
        ).apply {
            sdpSemantics = PeerConnection.SdpSemantics.UNIFIED_PLAN
        }
    }

    @Provides
    @Singleton
    fun provideEglBaseContext(): EglBase.Context {
        return EglBase.create().eglBaseContext
    }

    @Provides
    @Singleton
    fun provideVideoDecoderFactory(
        eglBaseContext: EglBase.Context,
    ): DefaultVideoDecoderFactory {
        return DefaultVideoDecoderFactory(eglBaseContext)
    }

    @Provides
    @Singleton
    fun provideVideoEncoderFactory(
        eglBaseContext: EglBase.Context,
    ): HardwareVideoEncoderFactory {
        return HardwareVideoEncoderFactory(eglBaseContext, true, true)
    }

    @Provides
    @Singleton
    fun providePeerConnectionFactory(
        videoDecoderFactory: DefaultVideoDecoderFactory,
        videoEncoderFactory: HardwareVideoEncoderFactory,
        @ApplicationContext context: Context,
    ): PeerConnectionFactory {
        PeerConnectionFactory.initialize(
            PeerConnectionFactory
                .InitializationOptions
                .builder(context)
                .createInitializationOptions(),
        )

        return PeerConnectionFactory
            .builder()
            .setVideoDecoderFactory(videoDecoderFactory)
            .setVideoEncoderFactory(videoEncoderFactory)
            .setAudioDeviceModule(
                JavaAudioDeviceModule
                    .builder(context)
                    .setUseHardwareAcousticEchoCanceler(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    .setUseHardwareNoiseSuppressor(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    .setAudioRecordErrorCallback(AudioDeviceErrorCallBack)
                    .setAudioTrackErrorCallback(AudioDeviceErrorCallBack)
                    .setAudioRecordStateCallback(AudioDeviceErrorCallBack)
                    .setAudioTrackStateCallback(AudioDeviceErrorCallBack)
                    .createAudioDeviceModule()
                    .also {
                        it.setMicrophoneMute(false)
                        it.setSpeakerMute(false)
                    },
            )
            .createPeerConnectionFactory()
    }

}