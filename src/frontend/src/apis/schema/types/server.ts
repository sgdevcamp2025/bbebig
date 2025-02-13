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
