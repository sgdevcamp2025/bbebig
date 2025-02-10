import { LOCAL_STORAGE } from '@/constants/keys'
import { VideoQuality } from '@/types/media'
import { create } from 'zustand'

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
  toggleVideoMute: () => void
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
    video: false,
    audioInput: false,
    audioOutput: false
  },
  videoQuality: 'HD'
} as const

const getInitialState = (): MediaSettings => {
  if (typeof window === 'undefined') {
    return DEFAULT_MEDIA_SETTINGS
  }

  return {
    devices: {
      video: localStorage.getItem(LOCAL_STORAGE.VIDEO) || null,
      audioInput: localStorage.getItem(LOCAL_STORAGE.AUDIO_INPUT) || null,
      audioOutput: localStorage.getItem(LOCAL_STORAGE.AUDIO_OUTPUT) || null
    },
    volume: {
      input: Number(localStorage.getItem(LOCAL_STORAGE.VOLUME_INPUT)) || 100,
      output: Number(localStorage.getItem(LOCAL_STORAGE.VOLUME_OUTPUT)) || 100
    },
    muted: {
      video: localStorage.getItem(LOCAL_STORAGE.MUTE_VIDEO) === 'true',
      audioInput: localStorage.getItem(LOCAL_STORAGE.MUTE_AUDIO_INPUT) === 'true',
      audioOutput: localStorage.getItem(LOCAL_STORAGE.MUTE_AUDIO_OUTPUT) === 'true'
    },
    videoQuality: (localStorage.getItem(LOCAL_STORAGE.VIDEO_QUALITY) as VideoQuality) || 'HD'
  }
}

const useMediaSettingsStore = create<MediaSettingsStore>((set) => ({
  ...getInitialState(),
  toggleAudioInputMute: () => {
    set((state) => {
      localStorage.setItem(LOCAL_STORAGE.MUTE_AUDIO_INPUT, String(!state.muted.audioInput))
      return {
        ...state,
        muted: { ...state.muted, audioInput: !state.muted.audioInput }
      }
    })
  },
  toggleAudioOutputMute: () => {
    set((state) => {
      localStorage.setItem(LOCAL_STORAGE.MUTE_AUDIO_OUTPUT, String(!state.muted.audioOutput))
      return {
        ...state,
        muted: { ...state.muted, audioOutput: !state.muted.audioOutput }
      }
    })
  },
  toggleVideoMute: () => {
    set((state) => {
      localStorage.setItem(LOCAL_STORAGE.MUTE_VIDEO, String(!state.muted.video))
      return {
        ...state,
        muted: { ...state.muted, video: !state.muted.video }
      }
    })
  },
  setInputDevice: (deviceId: string) => {
    set((state) => {
      const newValue = deviceId
      localStorage.setItem(LOCAL_STORAGE.AUDIO_INPUT, String(newValue))
      return {
        ...state,
        devices: { ...state.devices, audioInput: newValue }
      }
    })
  },
  setOutputDevice: (deviceId: string) => {
    set((state) => {
      localStorage.setItem(LOCAL_STORAGE.AUDIO_OUTPUT, String(deviceId))
      return {
        ...state,
        devices: { ...state.devices, audioOutput: deviceId }
      }
    })
  },
  setVideoDevice: (deviceId: string) => {
    set((state) => {
      localStorage.setItem(LOCAL_STORAGE.VIDEO, String(deviceId))
      return {
        ...state,
        devices: { ...state.devices, video: deviceId }
      }
    })
  },
  setInputVolume: (volume: number) => {
    set((state) => {
      localStorage.setItem(LOCAL_STORAGE.VOLUME_INPUT, String(volume))
      return {
        ...state,
        volume: { ...state.volume, input: volume }
      }
    })
  },
  setOutputVolume: (volume: number) => {
    set((state) => {
      localStorage.setItem(LOCAL_STORAGE.VOLUME_OUTPUT, String(volume))
      return {
        ...state,
        volume: {
          ...state.volume,
          output: volume
        }
      }
    })
  }
}))

export default useMediaSettingsStore
