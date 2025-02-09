import { useWebSocketStore } from '@/stores/use-web-socket-store'

export const useWebSocket = () => {
  const { connect, disconnect, sendMessage, subscribe, isConnected } = useWebSocketStore()

  return {
    connect,
    disconnect,
    sendMessage,
    subscribe,
    isConnected
  }
}
