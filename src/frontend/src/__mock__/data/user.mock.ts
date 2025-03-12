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
    friendId: 0,
    memberId: 0,
    memberName: 'test',
    memberNickname: 'test',
    memberAvatarUrl: null,
    memberBannerUrl: null,
    memberIntroduce: null,
    memberEmail: 'test@test1.com',
    globalStatus: 'ONLINE',
    createdAt: '2025-03-12T13:58:09.724Z'
  },
  {
    friendId: 1,
    memberId: 2,
    memberName: 'test',
    memberNickname: 'test',
    memberAvatarUrl: null,
    memberBannerUrl: null,
    memberIntroduce: null,
    memberEmail: 'test@test2.com',
    globalStatus: '',
    createdAt: '2025-03-12T13:58:09.724Z'
  },
  {
    friendId: 2,
    memberId: 3,
    memberName: 'test',
    memberNickname: 'test',
    memberAvatarUrl: null,
    memberBannerUrl: null,
    memberIntroduce: null,
    memberEmail: 'test@test3.com',
    globalStatus: 'ONLINE',
    createdAt: '2025-03-12T13:58:09.724Z'
  }
]
