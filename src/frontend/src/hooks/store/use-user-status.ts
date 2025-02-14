import { useShallow } from 'zustand/react/shallow'

import useUserStatusStore from '@/stores/use-user-status-store'

function useUserStatus() {
  return useUserStatusStore(
    useShallow((state) => ({
      channelInfo: state.channelInfo,
      joinVoiceChannel: state.joinVoiceChannel,
      leaveVoiceChannel: state.leaveVoiceChannel,
      getCurrentChannelInfo: state.getCurrentChannelInfo
    }))
  )
}

export default useUserStatus
