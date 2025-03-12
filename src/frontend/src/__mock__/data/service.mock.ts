import { CommonResponseType } from '@/apis/schema/types/common'
import {
  GetServerMemebersResponseSchema,
  GetServersResponseSchema
} from '@/apis/schema/types/service'

export const mockServersData = [
  {
    serverId: 1,
    serverName: 'Server 1',
    serverImageUrl: null
  },
  {
    serverId: 2,
    serverName: 'Server 2',
    serverImageUrl: null
  },
  {
    serverId: 3,
    serverName: 'Server 3',
    serverImageUrl: null
  },
  {
    serverId: 4,
    serverName: 'Server 4',
    serverImageUrl: null
  }
] as {
  serverId: number
  serverName: string
  serverImageUrl: string | null
}[]

export const mockCategoriesData = [
  {
    categoryId: 1,
    categoryName: '카테고리 1',
    position: 1
  },
  {
    categoryId: 2,
    categoryName: '카테고리 2',
    position: 2
  },
  {
    categoryId: 3,
    categoryName: '카테고리 3',
    position: 3
  }
]

export const mockChannelsData = [
  {
    channelId: 1,
    categoryId: 1,
    channelName: '채널 1',
    position: 1,
    channelType: 'TEXT',
    privateStatus: true,
    channelMemberIdList: [1, 2, 3],
    lastSequence: 0
  },
  {
    channelId: 2,
    categoryId: 1,
    channelName: '채널 2',
    position: 2,
    channelType: 'TEXT',
    privateStatus: true,
    channelMemberIdList: [1, 2, 3],
    lastSequence: 0
  }
] as {
  channelId: number
  channelName: string
  channelType: string
  privateStatus: boolean
  categoryId: number | null
  position: number
  channelMemberIdList: number[]
  lastSequence: number
}[]

export const mockServers = (
  categoryInfoList: typeof mockCategoriesData,
  channelInfoList: typeof mockChannelsData
) => ({
  code: 'SERVER_LIST_SUCCESS',
  message: '서버 목록 조회 성공',
  result: {
    serverId: 1,
    serverName: 'Server 1',
    ownerId: 1,
    serverImageUrl: null,
    categoryInfoList,
    channelInfoList
  }
})

export const mockServersList: CommonResponseType<GetServersResponseSchema> = {
  code: 'SERVER_LIST_SUCCESS',
  message: '서버 목록 조회 성공',
  result: { servers: mockServersData }
}

export const mockServerMembers: CommonResponseType<GetServerMemebersResponseSchema> = {
  code: 'SERVER_MEMBER_LIST_SUCCESS',
  message: '서버 멤버 목록 조회 성공',
  result: {
    serverId: 1,
    serverMemberInfoList: [
      {
        memberId: 0,
        nickName: 'test1',
        avatarUrl: null,
        bannerUrl: null,
        globalStatus: 'ONLINE'
      },
      {
        memberId: 1,
        nickName: 'test2',
        avatarUrl: null,
        bannerUrl: null,
        globalStatus: 'OFFLINE'
      },
      {
        memberId: 2,
        nickName: 'test3',
        avatarUrl: null,
        bannerUrl: null,
        globalStatus: 'INVISIBLE'
      }
    ]
  }
}

export const mockLastVisitChannelInfo = {
  code: 'CHANNEL_INFO_SUCCESS',
  message: '채널 정보 조회 성공',
  result: {
    serverId: 1,
    channelInfoList: [
      {
        channelId: 1,
        lastReadMessageId: 0,
        lastReadSequence: 0,
        lastAccessAt: '2025-03-12T13:19:13.221Z'
      },
      {
        channelId: 2,
        lastReadMessageId: 0,
        lastReadSequence: 0,
        lastAccessAt: '2025-03-12T13:19:13.221Z'
      },
      {
        channelId: 3,
        lastReadMessageId: 0,
        lastReadSequence: 0,
        lastAccessAt: '2025-03-12T13:19:13.221Z'
      }
    ]
  }
}

export const mockCategory = {
  categoryId: 1,
  serverId: 1,
  categoryName: '카테고리 1',
  categoryPosition: 1
}
