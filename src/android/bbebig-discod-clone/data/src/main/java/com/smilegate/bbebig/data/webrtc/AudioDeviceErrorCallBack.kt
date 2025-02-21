package com.smilegate.bbebig.data.webrtc

import org.webrtc.audio.JavaAudioDeviceModule

object AudioDeviceErrorCallBack : JavaAudioDeviceModule.AudioRecordErrorCallback,
    JavaAudioDeviceModule.AudioTrackErrorCallback, JavaAudioDeviceModule.AudioRecordStateCallback, JavaAudioDeviceModule.AudioTrackStateCallback {

    override fun onWebRtcAudioRecordInitError(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun onWebRtcAudioRecordStartError(
        p0: JavaAudioDeviceModule.AudioRecordStartErrorCode?,
        p1: String?,
    ) {
        TODO("Not yet implemented")
    }

    override fun onWebRtcAudioRecordError(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun onWebRtcAudioRecordStart() {
        TODO("Not yet implemented")
    }

    override fun onWebRtcAudioRecordStop() {
        TODO("Not yet implemented")
    }

    override fun onWebRtcAudioTrackInitError(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun onWebRtcAudioTrackStartError(
        p0: JavaAudioDeviceModule.AudioTrackStartErrorCode?,
        p1: String?,
    ) {
        TODO("Not yet implemented")
    }

    override fun onWebRtcAudioTrackError(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun onWebRtcAudioTrackStart() {
        TODO("Not yet implemented")
    }

    override fun onWebRtcAudioTrackStop() {
        TODO("Not yet implemented")
    }
}