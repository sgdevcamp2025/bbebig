import { ChannelType, ServerCategoryList } from '@/types/server'

export interface DeleteServerRequestSchema {
  serverId: number
}

export interface DeleteServerResponseSchema {
  serverId: number
}

export interface WithdrawServerRequestSchema {
  serverId: number
}

export interface WithdrawServerResponseSchema {
  serverId: number
}

export interface GetServerListRequestSchema {
  serverId: number
}

export interface GetServerListResponseSchema {
  serverId: number
  serverName: string
  ownerId: number
  serverImageUrl: string | null
  categoryInfoList: {
    categoryId: number
    categoryName: string
    position: number
    channelInfoList: {
      channelId: number
      categoryId: number
      position: number
      channelName: string
      channelType: ChannelType
      privateStatus: boolean
    }[]
  }[]
}

export interface GetServersResponseSchema {
  servers: ServerCategoryList[]
}

export interface GetMemberIdListInServerRequestSchema {
  serverId: number
}

export interface GetMemberIdListInServerResponseSchema {
  serverId: number
  ownerId: number
  memberIdList: number[]
}

export interface GetChannelIdListInServerRequestSchema {
  serverId: number
}

export interface GetChannelIdListInServerResponseSchema {
  serverId: number
  channelIdList: number[]
}

export interface GetMemberInfoLastVisitChannelRequestSchema {
  serverId: number
  memberId: number
}

export interface GetMemberInfoLastVisitChannelResponseSchema {
  serverId: number
  channelInfoList: {
    channelId: number
    lastReadMessageId: number
    lastAccessAt: string | Date
  }[]
}

export interface GetServerIdListWithMemberIdRequestSchema {
  memberId: number
}

export interface GetServerIdListWithMemberIdResponseSchema {
  memberId: number
  serverIdList: number[]
}

export interface CreateServerRequestSchema {
  serverName: string
  serverImageUrl: string | null
}

export interface CreateServerResponseSchema {
  serverId: number
}

export interface ParticipateServerRequestSchema {
  serverId: number
  memberNickname: string
  memberProfileImageUrl: string | null
}

export interface ParticipateServerResponseSchema {
  serverId: number
}

export interface UpdateServerRequestSchema {
  serverId: number
  serverName: string
  serverImageUrl: string | null
}

export interface UpdateServerResponseSchema {
  serverId: number
  serverName: string
  serverImageUrl: string | null
}

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

export interface HealthCheckResponseSchema {
  status: string
}

export interface LookUpRequestSchema {
  lookupKey: string
}

export interface LookUpErrorResponseSchema {
  error: string
}

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

export interface DeleteCategoryRequestSchema {
  categoryId: number
}

export interface DeleteCategoryResponseSchema {
  categoryId: number
}

export interface GetCategoriesRequestSchema {
  categoryId: number
}

export interface GetCategoriesResponseSchema {
  categoryId: number
  serverId: number
  categoryName: string
  categoryPosition: number
}

export interface CreateCategoryRequestSchema {
  serverId: number
  categoryName: string
}

export interface CreateCategoryResponseSchema {
  categoryId: number
}

export interface UpdateCategoryRequestSchema {
  categoryId: number
  categoryName: string
}

export interface UpdateCategoryResponseSchema {
  categoryId: number
  categoryName: string
}
