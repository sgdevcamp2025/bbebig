import { create } from 'zustand'
import { Client, IMessage } from '@stomp/stompjs'
import { createStompClient } from '@/apis/config/stomp-client'

interface WebSocketState {
  client: Client | null
  isConnected: boolean
  connect: () => void
  disconnect: () => void
  subscribe: (destination: string, callback: (message: any) => void) => void
  sendMessage: (destination: string, body: any) => void
}

export const useWebSocketStore = create<WebSocketState>((set, get) => ({
  client: null,
  isConnected: false,

  // 연결
  connect: () => {
    const stompClient = createStompClient()
    stompClient.onConnect = () => {
      console.log('Connected to WebSocket')
      set({ isConnected: true, client: stompClient })
    }

    stompClient.onDisconnect = () => {
      console.log('Disconnected from WebSocket')
      set({ isConnected: false, client: null })
    }

    stompClient.activate()
    set({ client: stompClient })
  },

  // 연결 종료
  disconnect: () => {
    const client = get().client
    if (client) {
      client.deactivate()
      set({ client: null, isConnected: false })
    }
  },

  // 메시지 구독
  subscribe: (destination, callback) => {
    const client = get().client
    if (client && get().isConnected) {
      client.subscribe(destination, (message: IMessage) => {
        callback(JSON.parse(message.body))
      })
    }
  },

  // 메시지 발행
  sendMessage: (destination, body) => {
    const client = get().client
    if (client && get().isConnected) {
      client.publish({
        destination,
        body: JSON.stringify(body)
      })
    }
  }
}))
