import { User } from './user'

export type ChannelType = 'TEXT' | 'VOICE'

export interface CategoryInfo {
  categoryId: number
  categoryName: string
  position: number
  channelInfoList: ChannelInfo[]
}

export interface ChannelInfo {
  channelId: number
  categoryId: number
  position: number
  channelName: string
  channelType: ChannelType
  privateStatus: boolean
}

export interface ChannelUser extends User {
  includeChannelId: number[]
}
