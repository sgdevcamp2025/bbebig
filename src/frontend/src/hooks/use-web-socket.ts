import { useWebSocketStore } from '@/stores/use-web-socket-store'
import { useEffect } from 'react'

export const useWebSocket = () => {
  const { connect, disconnect, sendMessage, subscribe, isConnected } = useWebSocketStore()

  useEffect(() => {
    connect()
    return () => {
      disconnect()
    }
  }, [])

  return {
    connect,
    disconnect,
    sendMessage,
    subscribe,
    isConnected
  }
}
