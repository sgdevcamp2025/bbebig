import { useShallow } from 'zustand/shallow'

import { useChatStompStore } from '@/stores/use-chatting-stomp-store'

function useChattingStomp() {
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

export default useChattingStomp
