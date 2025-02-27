import { Socket } from 'socket.io-client'
import io from 'socket.io-client'
import { create } from 'zustand'
import { useShallow } from 'zustand/shallow'

import { SIGNALING_NODE_SERVER_URL } from '@/constants/env'

interface SocketStore {
  client: Socket | null
  connect: () => void
  disconnect: () => void
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  subscribe: (destination: string, callback: (...args: any[]) => void) => void
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  send: (destination: string, ...args: any[]) => void
  unsubscribe: (destination: string) => void
}

const initialState: Socket = io(SIGNALING_NODE_SERVER_URL, {
  transports: ['websocket'],
  reconnection: true,
  reconnectionAttempts: 5,
  reconnectionDelay: 1000,
  reconnectionDelayMax: 5000
})

export const useSignalingSocket = create<SocketStore>((set, get) => ({
  client: null,
  // 연결
  connect: () => {
    const client = initialState

    client.on('connect', () => {
      set({ client: client })
    })

    client.on('disconnect', () => {
      set({ client: null })
    })
  },

  // 연결 종료
  disconnect: () => {
    let client = get().client
    if (!client) {
      client = initialState
      set({ client })
      client.connect()
    }

    client.disconnect()
    set({ client: null })
  },

  // 메시지 구독
  subscribe: (destination, callback) => {
    let client = get().client
    if (!client) {
      client = initialState
      set({ client })
      client.connect()
    }

    client.on(destination, (data: unknown) => {
      callback(data)
    })
  },

  // 메시지 발행
  send: (destination, ...args) => {
    let client = get().client
    if (!client) {
      client = initialState
      set({ client })
      client.connect()
    }

    client.emit(destination, ...args)
  },

  // 메시지 구독 취소
  unsubscribe: (destination) => {
    let client = get().client
    if (!client) {
      client = initialState
      set({ client })
      client.connect()
    }

    client.off(destination)
  }
}))

export const useSignalingSocketStore = () => {
  return useSignalingSocket(
    useShallow((state) => ({
      connect: state.connect,
      disconnect: state.disconnect,
      subscribe: state.subscribe,
      send: state.send,
      unsubscribe: state.unsubscribe
    }))
  )
}
