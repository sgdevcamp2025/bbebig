import { useState } from 'react'

import { AudioDevice } from '@/types/media'
import { log } from '@/utils/log'
import { errorLog } from '@/utils/log'

const DEFAULT_DEVICES: Record<MediaDeviceKind, AudioDevice> = {
  audioinput: {
    label: '기본 마이크 (시스템 설정)',
    value: 'default'
  },
  audiooutput: {
    label: '기본 스피커 (시스템 설정)',
    value: 'default'
  },
  videoinput: {
    label: '기본 카메라 (시스템 설정)',
    value: 'default'
  }
}

interface DevicesState {
  audioDevices: AudioDevice[]
  outputDevices: AudioDevice[]
  videoDevices: AudioDevice[]
}

export function useGetMediaDevices() {
  const [devices, setDevices] = useState<DevicesState>({
    audioDevices: [DEFAULT_DEVICES.audioinput],
    outputDevices: [DEFAULT_DEVICES.audiooutput],
    videoDevices: [DEFAULT_DEVICES.videoinput]
  })

  const getDevicesByType = async (kind: MediaDeviceKind): Promise<AudioDevice[]> => {
    const devices = await navigator.mediaDevices.enumerateDevices()
    const filteredDevices = devices
      .filter((device) => device.kind === kind)
      .map((device) => ({
        label: device.label || DEFAULT_DEVICES[kind].label,
        value: device.deviceId || DEFAULT_DEVICES[kind].value
      }))
    return filteredDevices.length ? filteredDevices : [DEFAULT_DEVICES[kind]]
  }

  async function initializeDevices() {
    try {
      await navigator.mediaDevices.enumerateDevices()

      // 오디오 권한 요청 시도
      try {
        const audioStream = await navigator.mediaDevices.getUserMedia({
          audio: true,
          video: false
        })

        // 오디오 장치 정보 업데이트
        const [audioDevices, outputDevices] = await Promise.all([
          getDevicesByType('audioinput'),
          getDevicesByType('audiooutput')
        ])

        setDevices((prev) => ({
          ...prev,
          audioDevices,
          outputDevices
        }))

        // 스트림 해제
        audioStream.getTracks().forEach((track) => track.stop())
      } catch (audioError) {
        errorLog('오디오 권한이 거부됨:', audioError)
        // 오디오 권한 거부 시 기본값 유지
      }

      // 비디오 권한 요청 시도
      try {
        const videoStream = await navigator.mediaDevices.getUserMedia({
          audio: false,
          video: true
        })

        // 비디오 권한 획득 성공
        const videoDevices = await getDevicesByType('videoinput')
        setDevices((prev) => ({
          ...prev,
          videoDevices
        }))

        // 스트림 해제
        videoStream.getTracks().forEach((track) => track.stop())
      } catch (videoError) {
        log('비디오 권한이 거부됨:', videoError)
        // 비디오 권한 거부 시 기본값 유지
      }
    } catch (error) {
      errorLog('장치 초기화 실패:', error)
      // 에러 발생 시 기본값 유지
    }
  }

  return {
    devices,
    initializeDevices
  }
}
