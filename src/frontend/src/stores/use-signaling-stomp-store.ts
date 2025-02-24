import { Client, IMessage } from '@stomp/stompjs'
import { create } from 'zustand'

import { SIGNALING_SERVER_URL } from '@/constants/env'
import { StompState } from '@/types/stomp'

export const useSignalingStomp = create<StompState>((set, get) => ({
  client: null,
  // 연결
  connect: () => {
    const client = new Client({
      brokerURL: SIGNALING_SERVER_URL,
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000
    })

    client.onConnect = () => {
      set({ client: client })
    }

    client.onDisconnect = () => {
      set({ client: null })
    }

    client.activate()
    set({ client: client })
  },

  // 연결 종료
  disconnect: () => {
    const client = get().client
    if (!client) throw new Error('Client is not connected')

    client.deactivate()
    set({ client: null })
  },

  // 메시지 구독
  subscribe: (subscriptionId, destination, callback) => {
    const client = get().client
    if (!client) throw new Error('Client is not connected')

    client.subscribe(
      destination,
      (message: IMessage) => {
        callback(JSON.parse(message.body))
      },
      {
        id: subscriptionId
      }
    )
  },

  // 메시지 발행
  send: (destination, body) => {
    const client = get().client
    if (!client) throw new Error('Client is not connected')

    client.publish({
      destination,
      body: JSON.stringify(body)
    })
  },

  // 메시지 구독 취소
  unsubscribe: (subscriptionId, destination) => {
    const client = get().client
    if (!client) throw new Error('Client is not connected')

    client.unsubscribe(destination, {
      id: subscriptionId
    })
  }
}))
