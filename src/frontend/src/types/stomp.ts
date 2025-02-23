/* eslint-disable @typescript-eslint/no-explicit-any */
// json 타입을 any로 허용
import { Client } from '@stomp/stompjs'

export interface StompState {
  client: Client | null
  isConnected: boolean
  connect: () => void
  disconnect: () => void
  subscribe: (destination: string, callback: (message: any) => void, id: string) => void
  unsubscribe: (destination: string, id: string) => void
  send: (destination: string, body: any) => void
}
