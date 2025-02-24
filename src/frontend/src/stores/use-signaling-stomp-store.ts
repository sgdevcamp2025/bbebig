import { IMessage } from '@stomp/stompjs'
import { create } from 'zustand'

import { signalingStompInstance } from '@/apis/config/stomp-instance'
import { StompState } from '@/types/stomp'

export const useSignalingStomp = create<StompState>((set, get) => ({
  client: null,
  isConnected: false,

  // 연결
  connect: () => {
    const client = signalingStompInstance()
    client.onConnect = () => {
      set({ isConnected: true, client: client })
    }

    client.onDisconnect = () => {
      set({ isConnected: false, client: null })
    }

    client.activate()
    set({ client: client })
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
  subscribe: (destination, callback, id) => {
    const client = get().client
    if (client && get().isConnected) {
      client.subscribe(
        destination,
        (message: IMessage) => {
          callback(JSON.parse(message.body))
        },
        {
          id
        }
      )
    }
  },

  // 메시지 발행
  send: (destination, body) => {
    const client = get().client
    if (client && get().isConnected) {
      client.publish({
        destination,
        body: JSON.stringify(body)
      })
    }
  },

  // 메시지 구독 취소
  unsubscribe: (destination, id) => {
    const client = get().client
    if (client) {
      client.unsubscribe(id, {
        destination
      })
    }
  }
}))
