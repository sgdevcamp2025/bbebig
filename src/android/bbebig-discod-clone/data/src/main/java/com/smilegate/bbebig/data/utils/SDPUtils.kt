package com.smilegate.bbebig.data.utils

import org.webrtc.SdpObserver
import org.webrtc.SessionDescription
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

suspend inline fun createValue(
  crossinline call: (SdpObserver) -> Unit
): Result<SessionDescription> = suspendCoroutine {
  val observer = object : SdpObserver {

    override fun onCreateSuccess(description: SessionDescription?) {
      if (description != null) {
        it.resume(Result.success(description))
      } else {
        it.resume(Result.failure(RuntimeException("SessionDescription is null!")))
      }
    }

    override fun onCreateFailure(message: String?) =
      it.resume(Result.failure(RuntimeException(message)))


    override fun onSetSuccess() = Unit
    override fun onSetFailure(p0: String?) = Unit
  }

  call(observer)
}

suspend inline fun setValue(
  crossinline call: (SdpObserver) -> Unit
): Result<Unit> = suspendCoroutine { continuation ->
  val observer = object : SdpObserver {

    override fun onCreateFailure(p0: String?) = Unit
    override fun onCreateSuccess(p0: SessionDescription?) = Unit

    override fun onSetSuccess() = continuation.resume(Result.success(Unit))
    override fun onSetFailure(message: String?) =
        continuation.resume(Result.failure(RuntimeException(message)))
  }

  call(observer)
}
