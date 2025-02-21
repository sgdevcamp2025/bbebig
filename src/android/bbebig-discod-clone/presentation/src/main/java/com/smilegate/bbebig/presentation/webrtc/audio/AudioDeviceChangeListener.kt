package com.smilegate.bbebig.presentation.webrtc.audio

typealias AudioDeviceChangeListener = (
    audioDevices: List<AudioDevice>,
    selectedAudioDevice: AudioDevice?,
) -> Unit
