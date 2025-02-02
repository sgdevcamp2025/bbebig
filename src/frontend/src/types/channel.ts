export type ChannelType = 'TEXT' | 'VOICE'

export interface Category {
  id: number
  name: string
  position: number
  channels: Channel[]
}

export interface Channel {
  id: number
  name: string
  position: number
  type: 'TEXT' | 'VOICE'
  privateStatus: boolean
}

export interface ChannelMember {
  id: number
  channelId: number
  serverMemberId: number
  createdAt: Date
  updatedAt: Date
}

export type ServerChannelList = Record<number, Category[]>
