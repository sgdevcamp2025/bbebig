import { useShallow } from 'zustand/shallow'

import { useChatStompStore } from '@/stores/use-chatting-stomp-store'

export const useChattingStomp = () => {
  return useChatStompStore(
    useShallow((state) => ({
      connect: state.connect,
      disconnect: state.disconnect,
      send: state.send,
      subscribe: state.subscribe,
      isConnected: state.isConnected
    }))
  )
}
