import { http, HttpResponse } from 'msw'

import {
  GetFriendListResponseSchema,
  GetFriendPendingListResponseSchema,
  GetUserSelfResponseSchema,
  UpdateUserPresenceStatusRequestSchema,
  UpdateUserRequestSchema
} from '@/apis/schema/types/user'
import { SERVER_URL } from '@/constants/env'

import { friendsMock, userMock } from '../data/user.mock'

const copyUserMock = { ...userMock }
let copyFriendsMock = [...friendsMock]
const copyFriendRequestsMock = [...friendsMock]

const MEMBER_PATH = '/user-server/members'
const FRIEND_PATH = '/user-server/friends'

export const userHandler = [
  http.delete(`${SERVER_URL}${MEMBER_PATH}`, () => {
    return new HttpResponse(
      JSON.stringify({
        code: 'MEMBER_DELETED',
        message: 'Member deleted',
        result: { memberId: 1 }
      })
    )
  }),
  http.get(`${SERVER_URL}${MEMBER_PATH}/:memberId`, ({ params }) => {
    const { memberId } = params as { memberId: string }
    return new HttpResponse(
      JSON.stringify({
        code: 'MEMBER_FOUND',
        message: 'Member found',
        result: {
          ...copyUserMock,
          memberId: Number(memberId)
        }
      })
    )
  }),
  http.get(`${SERVER_URL}${MEMBER_PATH}/self`, () => {
    return new HttpResponse(
      JSON.stringify({
        code: 'MEMBER_FOUND',
        message: 'Member found',
        result: userMock as GetUserSelfResponseSchema
      })
    )
  }),
  http.get(`${SERVER_URL}${MEMBER_PATH}/search`, ({ params }) => {
    const { nickname } = params as { nickname: string }
    return new HttpResponse(
      JSON.stringify({
        code: 'MEMBER_FOUND',
        message: 'Member found',
        result: {
          ...copyUserMock,
          nickname: nickname
        }
      })
    )
  }),
  http.patch(`${SERVER_URL}${MEMBER_PATH}/:memberId`, async ({ params, request }) => {
    const { memberId } = params as { memberId: string }
    const { name, nickname, birthdate, avatarUrl, bannerUrl, introduce } =
      (await request.json()) as UpdateUserRequestSchema
    copyUserMock.name = name
    copyUserMock.nickname = nickname
    copyUserMock.birthdate = birthdate
    copyUserMock.avatarUrl = avatarUrl ?? null
    copyUserMock.bannerUrl = bannerUrl ?? null
    copyUserMock.introduce = introduce ?? null
    return new HttpResponse(
      JSON.stringify({
        code: 'MEMBER_UPDATED',
        message: 'Member updated',
        result: {
          ...copyUserMock,
          memberId: Number(memberId)
        }
      })
    )
  }),
  http.patch(`${SERVER_URL}${MEMBER_PATH}/presence`, async ({ request }) => {
    const { customPresenceStatus } = (await request.json()) as UpdateUserPresenceStatusRequestSchema
    copyUserMock.presenceStatus = customPresenceStatus
    return new HttpResponse(
      JSON.stringify({
        code: 'MEMBER_UPDATED',
        message: 'Member updated',
        result: copyUserMock
      })
    )
  }),
  http.delete(`${SERVER_URL}${FRIEND_PATH}/:friendId`, ({ params }) => {
    const { friendId } = params as { friendId: string }
    copyFriendsMock = copyFriendsMock.filter((friend) => friend.friendId !== Number(friendId))
    return new HttpResponse(
      JSON.stringify({
        code: 'FRIEND_DELETED',
        message: 'Friend deleted',
        result: {
          id: Number(friendId)
        }
      })
    )
  }),
  http.delete(`${SERVER_URL}${FRIEND_PATH}/:friendId/cancel`, ({ params }) => {
    const { friendId } = params as { friendId: string }
    copyFriendsMock = copyFriendsMock.filter((friend) => friend.friendId !== Number(friendId))
    return new HttpResponse(
      JSON.stringify({
        code: 'FRIEND_CANCELLED',
        message: 'Friend cancelled',
        result: {
          id: Number(friendId)
        }
      })
    )
  }),
  http.get(`${SERVER_URL}${FRIEND_PATH}`, ({ params }) => {
    const { memberId } = params as { memberId: string }
    return new HttpResponse(
      JSON.stringify({
        code: 'FRIEND_FOUND',
        message: 'Friend found',
        result: {
          memberId: Number(memberId),
          friendsCount: copyFriendRequestsMock.length,
          friends: copyFriendRequestsMock
        } as GetFriendListResponseSchema
      })
    )
  }),
  http.get(`${SERVER_URL}${FRIEND_PATH}/pending`, ({ params }) => {
    const { memberId } = params as { memberId: string }
    return new HttpResponse(
      JSON.stringify({
        code: 'FRIEND_FOUND',
        message: 'Friend found',
        result: {
          memberId: Number(memberId),
          pendingFriendsCount: copyFriendRequestsMock.length,
          receivePendingFriendsCount: copyFriendRequestsMock.length,
          sendPendingFriends: copyFriendRequestsMock,
          receivePendingFriends: copyFriendRequestsMock
        } as GetFriendPendingListResponseSchema
      })
    )
  }),
  http.patch(`${SERVER_URL}${FRIEND_PATH}/:friendId/declined`, ({ params }) => {
    const { friendId } = params as { friendId: string }
    return new HttpResponse(
      JSON.stringify({
        code: 'FRIEND_PENDING',
        message: 'Friend pending',
        result: {
          id: Number(friendId),
          friendStatus: 'PENDING'
        }
      })
    )
  }),
  http.patch(`${SERVER_URL}${FRIEND_PATH}/:friendId/accepted`, ({ params }) => {
    const { friendId } = params as { friendId: string }
    return new HttpResponse(
      JSON.stringify({
        code: 'FRIEND_ACCEPTED',
        message: 'Friend accepted',
        result: {
          id: Number(friendId),
          friendStatus: 'ACCEPTED'
        }
      })
    )
  }),
  http.post(`${SERVER_URL}${FRIEND_PATH}`, () => {
    const randomId = Math.floor(Math.random() * 1000000)
    const randomFromMemberId = Math.floor(Math.random() * 1000000)
    const randomToMemberId = Math.floor(Math.random() * 1000000)
    return new HttpResponse(
      JSON.stringify({
        code: 'FRIEND_REQUESTED',
        message: 'Friend requested',
        result: {
          friendId: randomId,
          fromMemberId: randomFromMemberId,
          toMemeberId: randomToMemberId,
          createdAt: new Date().toISOString()
        }
      })
    )
  })
]
