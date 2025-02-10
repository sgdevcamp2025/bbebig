import { useShallow } from 'zustand/shallow'

import { useWebSocketStore } from '@/stores/use-web-socket-store'

export const useWebSocket = () => {
  const { connect, disconnect, sendMessage, subscribe, isConnected } = useWebSocketStore(
    useShallow((state) => ({
      connect: state.connect,
      disconnect: state.disconnect,
      sendMessage: state.sendMessage,
      subscribe: state.subscribe,
      isConnected: state.isConnected
    }))
  )

  return {
    connect,
    disconnect,
    sendMessage,
    subscribe,
    isConnected
  }
}
