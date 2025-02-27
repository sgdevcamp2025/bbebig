/* eslint-disable @typescript-eslint/no-explicit-any */
// json 타입을 any로 허용
import { Client } from '@stomp/stompjs'

export interface StompState {
  client: Client | null
  connect: () => void
  disconnect: () => void
  subscribe: (subscriptionId: string, destination: string, callback: (message: any) => void) => void
  unsubscribe: (subscriptionId: string, destination: string) => void
  send: (destination: string, body: any) => void
}
