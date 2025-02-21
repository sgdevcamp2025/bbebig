package com.smilegate.bbebig.data.utils

import org.webrtc.AddIceObserver
import org.webrtc.IceCandidate
import org.webrtc.PeerConnection
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend fun PeerConnection.addRtcIceCandidate(iceCandidate: IceCandidate): Result<Unit> {
  return suspendCoroutine { cont ->
    addIceCandidate(
      iceCandidate,
      object : AddIceObserver {
        override fun onAddSuccess() {
          cont.resume(Result.success(Unit))
        }

        override fun onAddFailure(error: String?) {
          cont.resume(Result.failure(RuntimeException(error)))
        }
      }
    )
  }
}
