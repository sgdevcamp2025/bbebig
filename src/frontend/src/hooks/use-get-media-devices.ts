import { AudioDevice } from '@/types/media'
import { useEffect, useState } from 'react'

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

// 사용자의 디바이스 정보를 불러올 수 있는 훅
function useGetMediaDevices() {
  const [devices, setDevices] = useState<DevicesState>({
    audioDevices: [DEFAULT_DEVICES.audioinput],
    outputDevices: [DEFAULT_DEVICES.audiooutput],
    videoDevices: [DEFAULT_DEVICES.videoinput]
  })

  const getMediaDevices = async (): Promise<MediaDeviceInfo[]> => {
    const devices = await navigator.mediaDevices.enumerateDevices()

    return devices
  }

  const filterDevicesByKind = (
    devices: MediaDeviceInfo[],
    kind: MediaDeviceKind
  ): AudioDevice[] => {
    return devices
      .filter((device) => device.kind === kind)
      .map((device) => ({
        label: device.label,
        value: device.deviceId
      }))
  }

  const getDevicesByType = async (kind: MediaDeviceKind): Promise<AudioDevice[]> => {
    const devices = await getMediaDevices()
    const filteredDevices = filterDevicesByKind(devices, kind)
    return filteredDevices.length ? filteredDevices : [DEFAULT_DEVICES[kind]]
  }

  useEffect(() => {
    async function initializeDevices() {
      try {
        const stream = await navigator.mediaDevices.getUserMedia({
          audio: true,
          video: true
        })
        stream.getTracks().forEach((track) => track.stop())

        const [audioDevices, outputDevices, videoDevices] = await Promise.all([
          getDevicesByType('audioinput'),
          getDevicesByType('audiooutput'),
          getDevicesByType('videoinput')
        ])

        setDevices({ audioDevices, outputDevices, videoDevices })
      } catch (error) {
        console.error('디바이스 초기화 실패', error)
        setDevices({
          audioDevices: [DEFAULT_DEVICES.audioinput],
          outputDevices: [DEFAULT_DEVICES.audiooutput],
          videoDevices: [DEFAULT_DEVICES.videoinput]
        })
      }
    }

    navigator.mediaDevices.addEventListener('devicechange', () => {
      initializeDevices()
    })
    initializeDevices()
    return () => {
      navigator.mediaDevices.removeEventListener('devicechange', initializeDevices)
    }
  }, [])

  return {
    devices,
    getAudioDevices: () => getDevicesByType('audioinput'),
    getOutputDevices: () => getDevicesByType('audiooutput'),
    getVideoDevices: () => getDevicesByType('videoinput')
  }
}

export default useGetMediaDevices
