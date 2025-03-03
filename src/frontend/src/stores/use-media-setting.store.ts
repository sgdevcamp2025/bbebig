import { create } from 'zustand'

import { VideoQuality } from '@/types/media'

interface MediaSettings {
  devices: {
    video: string | null
    audioInput: string | null
    audioOutput: string | null
  }
  volume: {
    input: number
    output: number
  }
  muted: {
    video: boolean
    audioInput: boolean
    audioOutput: boolean
  }
  videoQuality: VideoQuality
}

type MediaSettingsStore = MediaSettings & {
  setVideoMute: () => void
  toggleAudioInputMute: () => void
  toggleAudioOutputMute: () => void
  setInputDevice: (deviceId: string) => void
  setOutputDevice: (deviceId: string) => void
  setVideoDevice: (deviceId: string) => void
  setInputVolume: (volume: number) => void
  setOutputVolume: (volume: number) => void
}

const DEFAULT_MEDIA_SETTINGS: MediaSettings = {
  devices: {
    video: null,
    audioInput: null,
    audioOutput: null
  },
  volume: {
    input: 50,
    output: 50
  },
  muted: {
    video: true,
    audioInput: true,
    audioOutput: true
  },
  videoQuality: 'HD'
} as const

export const useMediaSettingsStore = create<MediaSettingsStore>((set) => ({
  ...DEFAULT_MEDIA_SETTINGS,
  toggleAudioInputMute: () => {
    set((state) => {
      return { ...state, muted: { ...state.muted, audioInput: !state.muted.audioInput } }
    })
  },
  toggleAudioOutputMute: () => {
    set((state) => {
      return { ...state, muted: { ...state.muted, audioOutput: !state.muted.audioOutput } }
    })
  },
  setVideoMute: () => {
    set((state) => {
      return { ...state, muted: { ...state.muted, video: !state.muted.video } }
    })
  },
  setInputDevice: (deviceId: string) => {
    set((state) => {
      return { ...state, devices: { ...state.devices, audioInput: deviceId } }
    })
  },
  setOutputDevice: (deviceId: string) => {
    set((state) => {
      return { ...state, devices: { ...state.devices, audioOutput: deviceId } }
    })
  },
  setVideoDevice: (deviceId: string) => {
    set((state) => {
      return { ...state, devices: { ...state.devices, video: deviceId } }
    })
  },
  setInputVolume: (volume: number) => {
    set((state) => {
      return { ...state, volume: { ...state.volume, input: volume } }
    })
  },
  setOutputVolume: (volume: number) => {
    set((state) => {
      return { ...state, volume: { ...state.volume, output: volume } }
    })
  }
}))
