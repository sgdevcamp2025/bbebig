import { IMessage } from '@stomp/stompjs'
import { create } from 'zustand'

import { signalingStompInstance } from '@/apis/config/stomp-instance'
import { StompState } from '@/types/stomp'

export const useSignalingStompStore = create<StompState>((set, get) => ({
  client: null,
  isConnected: false,

  // 연결
  connect: () => {
    const client = signalingStompInstance()
    client.onConnect = () => {
      console.log('[✅] 시그널링 서버 연결 성공')
      set({ isConnected: true, client: client })
    }

    client.onDisconnect = () => {
      console.log('[❌] 시그널링 서버 연결 실패')
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
      console.log('[❌] 시그널링 서버 연결 종료')
      set({ client: null, isConnected: false })
    }
  },

  // 메시지 구독
  subscribe: (destination, callback) => {
    const client = get().client
    if (client && get().isConnected) {
      client.subscribe(destination, (message: IMessage) => {
        console.log('[✅] 시그널링 서버 메시지 구독 : ', message)
        callback(JSON.parse(message.body))
      })
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
      console.log('[✅] 시그널링 서버 메시지 발행 : ', destination, body)
    }
  },

  // 메시지 발행
  message: (destination, body) => {
    const client = get().client
    if (client && get().isConnected) {
      client.publish({
        destination,
        body: JSON.stringify(body)
      })
    }
  },

  // 메시지 구독 취소
  unsubscribe: (destination) => {
    const client = get().client
    if (client && get().isConnected) {
      client.unsubscribe(destination)
    }
  }
}))
