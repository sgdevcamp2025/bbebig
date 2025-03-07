import { create } from 'zustand'
import { useShallow } from 'zustand/shallow'

interface ChannelInfo {
  channelId: number
  channelName: string | null
  serverName: string | null
}

interface UserStatusStore {
  channelInfo: ChannelInfo | null
  joinVoiceChannel: (channelInfo: ChannelInfo) => void
  leaveVoiceChannel: () => void
  getCurrentChannelInfo: () => ChannelInfo | null
}

const useUserStatusStore = create<UserStatusStore>((set, get) => ({
  channelInfo: null,
  joinVoiceChannel: (channelInfo: ChannelInfo) => set({ channelInfo }),
  leaveVoiceChannel: () => set({ channelInfo: null }),
  getCurrentChannelInfo: () => get().channelInfo
}))

export function useUserStatus() {
  return useUserStatusStore(
    useShallow((state) => ({
      channelInfo: state.channelInfo,
      joinVoiceChannel: state.joinVoiceChannel,
      leaveVoiceChannel: state.leaveVoiceChannel,
      getCurrentChannelInfo: state.getCurrentChannelInfo
    }))
  )
}
