import { ChannelType } from '@/types/server'

export interface DeleteChannelRequestSchema {
  channelId: number
}

export interface DeleteChannelResponseSchema {
  channelId: number
}

export interface GetChannelListRequestSchema {
  channelId: number
}

export interface GetChannelListResponseSchema {
  channelId: number
  channelName: string
  channelPosition: number
  channelType: ChannelType
  privateStatus: boolean
}

export interface GetChannelLastVisitInfoWithMemberIdRequestSchema {
  channelId: number
  memberId: number
}

export interface GetChannelLastVisitInfoWithMemberIdResponseSchema {
  channelId: number
  lastReadMessageId: number
  lastAccessAt: string | Date
}

export interface CreateChannelRequestSchema {
  serverId: number
  categoryId: number | undefined
  channelType: ChannelType
  channelName: string
  privateStatus: boolean
  memberIds: number[]
}

export interface CreateChannelResponseSchema {
  channelId: number
}

export interface UpdateChannelRequestSchema {
  channelId: number
  channelName: string
  privateStatus: boolean
  memberIds: number[]
}

export interface UpdateChannelResponseSchema {
  channelId: number
  channelName: string
  privateStatus: boolean
}
