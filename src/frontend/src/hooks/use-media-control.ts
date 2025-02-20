import { useRef } from 'react'
import { useShallow } from 'zustand/react/shallow'

import { useMediaSettingsStore } from '@/stores/use-media-setting.store'

function useMediaControl() {
  const videoRef = useRef<HTMLVideoElement>(null)
  const streamRef = useRef<MediaStream | null>(null)

  const { muted } = useMediaSettingsStore(
    useShallow((state) => ({
      muted: state.muted
    }))
  )

  const startStream = async (constraint?: MediaStreamConstraints) => {
    try {
      const stream = await navigator.mediaDevices.getUserMedia(constraint)
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
  }

  const stopStream = ({ audio, video }: { audio?: boolean; video?: boolean }) => {
    if (streamRef.current) {
      streamRef.current.getTracks().forEach((track) => {
        if (audio && track.kind === 'audio') {
          track.enabled = false
        }
        if (video && track.kind === 'video') {
          track.enabled = false
        }
      })
      streamRef.current = null
    }
    if (videoRef.current) {
      videoRef.current.srcObject = null
      videoRef.current.pause()
    }
  }

  const toggleVideo = () => {
    if (streamRef.current) {
      streamRef.current.getVideoTracks().forEach((track) => {
        track.enabled = !muted.video
      })
    }
  }

  const toggleAudio = () => {
    if (streamRef.current) {
      streamRef.current.getAudioTracks().forEach((track) => {
        track.enabled = !muted.audioInput
      })
    }
  }

  const getStream = () => {
    return navigator.mediaDevices.getUserMedia({
      video: true,
      audio: true
    })
  }

  return {
    videoRef,
    streamRef,
    startStream,
    stopStream,
    getStream,
    toggleVideo,
    toggleAudio
  }
}

export default useMediaControl
