import { ChannelType } from '@/types/channel'

export interface CreateCategoryRequestSchema {
  categoryName: string
  serverId: string
}

export interface CreateCategoryResponseSchema {
  id: string
  name: string
  description: string
  serverId: string
}

export interface CreateChannelRequestSchema {
  channelName: string
  categoryId?: string
  channelType: ChannelType
}

export interface CreateChannelResponseSchema {
  id: string
  name: string
  categoryId: string
}
