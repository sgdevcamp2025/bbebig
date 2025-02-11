import { useShallow } from 'zustand/shallow'

import { useSignalingStompStore } from '@/stores/use-signaling-stomp-store'

function useSignalingStomp() {
  return useSignalingStompStore(
    useShallow((state) => ({
      connect: state.connect,
      disconnect: state.disconnect,
      send: state.send,
      subscribe: state.subscribe,
      isConnected: state.isConnected
    }))
  )
}

export default useSignalingStomp
