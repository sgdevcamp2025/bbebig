package com.smilegate.bbebig.presentation.webrtc.audio


import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.media.AudioDeviceInfo
import android.media.AudioFocusRequest
import android.media.AudioManager

internal class AudioManagerAdapterImpl(
  private val context: Context,
  private val audioManager: AudioManager,
  private val audioFocusRequest: AudioFocusRequestWrapper = AudioFocusRequestWrapper(),
  private val audioFocusChangeListener: AudioManager.OnAudioFocusChangeListener
) : AudioManagerAdapter {

  private var savedAudioMode = 0
  private var savedIsMicrophoneMuted = false
  private var savedSpeakerphoneEnabled = false
  private var audioRequest: AudioFocusRequest? = null

  init {
  }

  override fun hasEarpiece(): Boolean {
    return context.packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)
  }

  @SuppressLint("NewApi")
  override fun hasSpeakerphone(): Boolean {
    return if (context.packageManager.hasSystemFeature(PackageManager.FEATURE_AUDIO_OUTPUT)) {
      val devices = audioManager.getDevices(AudioManager.GET_DEVICES_OUTPUTS)
      for (device in devices) {
        if (device.type == AudioDeviceInfo.TYPE_BUILTIN_SPEAKER) {
          return true
        }
      }
      false
    } else {
      true
    }
  }

  @SuppressLint("NewApi")
  override fun setAudioFocus() {
    // Request audio focus before making any device switch.
      audioRequest = audioFocusRequest.buildRequest(audioFocusChangeListener)
      audioRequest?.let {
        val result = audioManager.requestAudioFocus(it)
      }

    audioManager.mode = AudioManager.MODE_IN_COMMUNICATION
  }

  override fun enableBluetoothSco(enable: Boolean) {
    audioManager.run { if (enable) startBluetoothSco() else stopBluetoothSco() }
  }

  override fun enableSpeakerphone(enable: Boolean) {
    audioManager.isSpeakerphoneOn = enable
  }

  override fun mute(mute: Boolean) {
    audioManager.isMicrophoneMute = mute
  }

  // TODO Consider persisting audio state in the event of process death
  override fun cacheAudioState() {
    savedAudioMode = audioManager.mode
    savedIsMicrophoneMuted = audioManager.isMicrophoneMute
    savedSpeakerphoneEnabled = audioManager.isSpeakerphoneOn
  }

  @SuppressLint("NewApi")
  override fun restoreAudioState() {
    audioManager.mode = savedAudioMode
    mute(savedIsMicrophoneMuted)
    enableSpeakerphone(savedSpeakerphoneEnabled)
      audioRequest?.let {
        audioManager.abandonAudioFocusRequest(it)
      }
  }
}
