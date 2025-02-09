import { useCallback, useEffect, useRef } from 'react'
import useMediaSettingsStore from '@/stores/use-media-setting.store'
import { useShallow } from 'zustand/react/shallow'

function useMediaControl() {
  const videoRef = useRef<HTMLVideoElement>(null)
  const streamRef = useRef<MediaStream | null>(null)

  const { muted, devices } = useMediaSettingsStore(
    useShallow((state) => ({
      muted: state.muted,
      devices: state.devices
    }))
  )

  const startStream = useCallback(async () => {
    try {
      const permissions = await navigator.mediaDevices.getUserMedia({
        video: true
      })

      permissions.getTracks().forEach((track) => track.stop())

      const stream = await navigator.mediaDevices.getUserMedia({
        video: {
          deviceId: devices.video || undefined
        },
        audio: false
      })

      if (videoRef.current) {
        videoRef.current.srcObject = stream
      }
      streamRef.current = stream

      stream.getVideoTracks().forEach((track) => {
        track.enabled = !muted.video
      })
    } catch (error) {
      console.error('비디오 스트림 시작 실패:', error)
      throw error
    }
  }, [devices.video, muted.video])

  const stopStream = useCallback(() => {
    if (streamRef.current) {
      streamRef.current.getTracks().forEach((track) => track.stop())
      streamRef.current = null
    }
    if (videoRef.current) {
      videoRef.current.srcObject = null
    }
  }, [])

  useEffect(() => {
    if (streamRef.current) {
      streamRef.current.getVideoTracks().forEach((track) => {
        track.enabled = !muted.video
      })
    }
  }, [muted.video])

  useEffect(() => {
    return () => {
      stopStream()
    }
  }, [stopStream])

  return {
    videoRef,
    streamRef,
    startStream,
    stopStream
  }
}

export default useMediaControl
