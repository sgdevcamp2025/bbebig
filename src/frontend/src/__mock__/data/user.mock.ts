import { User } from '@sentry/react'

export const userMock = {
  id: 1,
  name: 'test',
  nickname: 'test',
  email: 'test@test.com',
  birthdate: '2021-01-01',
  avatarUrl: null,
  bannerUrl: null,
  introduce: null,
  customPresenceStatus: 'ONLINE',
  lastAccessAt: '2021-01-01T00:00:00.000Z'
} as User

export const friendsMock = [
  {
    friendId: 1,
    memberId: 2,
    memberName: '김테스트',
    memberNickname: '테슷트2',
    memberAvatarUrl: null,
    memberBannerUrl: null,
    memberIntroduce: null,
    memberEmail: 'test@test2.com',
    globalStatus: 'OFFLINE',
    createdAt: null
  },
  {
    friendId: 6,
    memberId: 4,
    memberName: '테3',
    memberNickname: '테3',
    memberAvatarUrl: null,
    memberBannerUrl: null,
    memberIntroduce: null,
    memberEmail: 'test@test3.com',
    globalStatus: 'OFFLINE',
    createdAt: null
  }
]
