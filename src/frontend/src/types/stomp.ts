/* eslint-disable @typescript-eslint/no-explicit-any */
// json 타입을 any로 허용
import { Client } from '@stomp/stompjs'

export interface StompState {
  client: Client | null
  isConnected: boolean
  connect: () => void
  disconnect: () => void
  subscribe: (destination: string, callback: (message: any) => void) => void
  unsubscribe: (destination: string) => void
  send: (destination: string, body: any) => void
  message: (destination: string, body: any) => void
}

// 서버 채널 메시지 타입
export interface ChatMessageRequest {
  id: number
  channelType: 'CHANNEL' | 'DM'
  messageType: 'TEXT' | 'IMAGE' | 'VIDEO' | 'FILE' | 'SYSTEM'
  type: 'MESSAGE_CREATE' | 'MESSAGE_UPDATE' | 'MESSAGE_DELETE'
  serverId: number
  channelId: number
  sendMemberId: number
  content: string
  createdAt?: string
  updatedAt?: string
}

// 채널 입/퇴장
export interface ChannelPresenceRequest {
  memberId: number
  type: 'ENTER' | 'LEAVE'
  channelType: 'CHANNEL' | 'DM'
  serverId: number
  channelId: number
  lastReadMessageId?: string
  eventTime: string
}

// 타이핑 상태 요청
export interface TypingRequest {
  memberId: number
  type: 'TYPING_START'
  memberNickname: string
  serverId: number
  channelId: number
}
