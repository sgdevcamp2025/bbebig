import io, { Socket } from 'socket.io-client'
import { create } from 'zustand'

import { SIGNALING_NODE_SERVER_URL } from '@/constants/env'
import { errorLog } from '@/utils/log'

interface SignalingSocketStore {
  socket: Socket | null
  connect: () => void
  disconnect: () => void
  on: <T extends unknown[]>(event: string, callback: (...args: T) => void) => void
  off: <T extends unknown[]>(event: string, callback: (...args: T) => void) => void
  emit: <T extends unknown[]>(event: string, ...args: T) => void
}

export const useSignalingSocket = create<SignalingSocketStore>((set, get) => ({
  socket: null,
  connect: () => {
    const socket = io(SIGNALING_NODE_SERVER_URL, {
      transports: ['websocket'],
      forceNew: true,
      autoConnect: true,
      reconnectionAttempts: 3
    })

    socket.on('connect_error', (error) => {
      errorLog('Socket connection error:', error)
    })

    set({ socket })
  },
  disconnect: () => {
    const socket = get().socket
    if (socket) {
      socket.disconnect()
      set({ socket: null })
    }
  },
  on: <T extends unknown[]>(event: string, callback: (...args: T) => void) => {
    const socket = get().socket
    if (socket) {
      socket.on(event, callback)
    }
  },
  off: <T extends unknown[]>(event: string, callback: (...args: T) => void) => {
    const socket = get().socket
    if (socket) {
      socket.off(event, callback)
    }
  },
  emit: <T extends unknown[]>(event: string, ...args: T) => {
    const socket = get().socket
    if (socket) {
      socket.emit(event, ...args)
    }
  }
}))
