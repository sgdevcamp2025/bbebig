import { create } from 'zustand'
import { useShallow } from 'zustand/react/shallow'

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

const useUserChannelStatusStore = create<UserStatusStore>((set, get) => ({
  channelInfo: null,
  joinVoiceChannel: (channelInfo: ChannelInfo) => set({ channelInfo }),
  leaveVoiceChannel: () => set({ channelInfo: null }),
  getCurrentChannelInfo: () => get().channelInfo
}))

export const useUserChannelStatus = () => {
  return useUserChannelStatusStore(
    useShallow((state) => ({
      channelInfo: state.channelInfo,
      joinVoiceChannel: state.joinVoiceChannel,
      leaveVoiceChannel: state.leaveVoiceChannel,
      getCurrentChannelInfo: state.getCurrentChannelInfo
    }))
  )
}
