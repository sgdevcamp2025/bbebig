import { useCallback, useEffect, useRef } from 'react'
import { useShallow } from 'zustand/react/shallow'

import useMediaSettingsStore from '@/stores/use-media-setting.store'

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
          deviceId: devices.video || undefined,
          width: 1920,
          height: 1080
        },
        audio: {
          deviceId: devices.audioInput || undefined
        }
      })

      stream.getTracks().forEach((track) => {
        track.enabled = true
      })

      if (videoRef.current) {
        videoRef.current.srcObject = stream
      }

      streamRef.current = stream
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
      videoRef.current.pause()
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

  const getStream = (constraint?: MediaStreamConstraints) => {
    return navigator.mediaDevices.getUserMedia({
      video: constraint?.video,
      audio: constraint?.audio
    })
  }

  return {
    videoRef,
    streamRef,
    startStream,
    stopStream,
    getStream
  }
}

export default useMediaControl
