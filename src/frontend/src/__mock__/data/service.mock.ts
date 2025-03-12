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
  }
] as {
  serverId: number
  serverName: string
  serverImageUrl: string | null
}[]

export const mockCategoriesData = [
  {
    categoryId: 1,
    categoryName: '채팅 채널',
    position: 1
  },
  {
    categoryId: 2,
    categoryName: '음성 채널',
    position: 2
  }
]

export const mockChannelsData = [
  {
    channelId: 1,
    categoryId: 1,
    channelName: '일반',
    position: 1,
    channelType: 'CHAT',
    privateStatus: false,
    channelMemberIdList: [1, 2, 3, 4],
    lastSequence: 0
  },
  {
    channelId: 2,
    categoryId: 2,
    channelName: '일반',
    position: 1,
    channelType: 'VOICE',
    privateStatus: false,
    channelMemberIdList: [1, 2, 3, 4],
    lastSequence: 0
  },
  {
    channelId: 13,
    categoryId: 1,
    channelName: '채널2',
    position: 2,
    channelType: 'CHAT',
    privateStatus: false,
    channelMemberIdList: [1, 2, 3, 4],
    lastSequence: 2
  },
  {
    channelId: 16,
    categoryId: 1,
    channelName: 'ㅁㄴㅇㅁㄴㅇ',
    position: 3,
    channelType: 'CHAT',
    privateStatus: false,
    channelMemberIdList: [1, 2, 3, 4],
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

export const mockServers = {
  code: 'SERVER_LIST_SUCCESS',
  message: '서버 목록 조회 성공',
  result: {
    serverId: 1,
    serverName: 'Server 1',
    ownerId: 1,
    serverImageUrl: null,
    categoryInfoList: mockCategoriesData,
    channelInfoList: mockChannelsData
  }
}

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
        memberId: 1,
        nickName: '테스트1',
        avatarUrl: null,
        bannerUrl: null,
        joinAt: '2025-02-27T14:04:50.258047',
        globalStatus: 'ONLINE'
      },
      {
        memberId: 2,
        nickName: '테슷트2',
        avatarUrl: null,
        bannerUrl: null,
        joinAt: '2025-02-27T14:09:57.037225',
        globalStatus: 'OFFLINE'
      },
      {
        memberId: 3,
        nickName: '서테스트',
        avatarUrl: null,
        bannerUrl: null,
        joinAt: '2025-02-27T14:46:42.392247',
        globalStatus: 'OFFLINE'
      },
      {
        memberId: 4,
        nickName: '테3',
        avatarUrl: null,
        bannerUrl: null,
        joinAt: '2025-02-27T23:27:00.269422',
        globalStatus: 'OFFLINE'
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
