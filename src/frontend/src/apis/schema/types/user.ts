import { FriendStatus } from '@/types/friend'
import { CustomPresenceStatus, User } from '@/types/user'

export interface DeleteMemberResponseSchema {
  id: number
}

export interface GetUserRequestSchema {
  memberId: number
}

export type GetUserResponseSchema = User

export type GetUserSelfResponseSchema = User

export interface UpdateUserRequestSchema {
  name: string
  nickname: string
  birthdate: string
  avatarUrl: string
  bannerUrl: string
  introduce: string
}

export interface UpdateUserResponseSchema {
  user: Omit<User, 'customPresenceStatus' | 'email' | 'lastAccessAt'>
}

export interface UpdateUserPresenceStatusRequestSchema {
  customPresenceStatus: CustomPresenceStatus
}

export interface UpdateUserPresenceStatusResponseSchema {
  id: number
  customPresenceStatus: CustomPresenceStatus
}

export interface DeleteFriendRequestSchema {
  friendId: number
}

export interface DeleteFriendResponseSchema {
  id: number
}

export interface DeleteFriendInRequestListRequestSchema {
  friendId: number
}

export interface DeleteFriendInRequestListResponseSchema {
  friendId: number
}

export interface GetFriendPendingListResponseSchema {
  friendId: number
  friendMemberId: number
  friendName: string
  friendNickname: string
  friendAvatarUrl: string
  friendBannerUrl: string
  friendIntroduce: string
  friendEmail: string
}

export interface GetFriendAcceptedListResponseSchema {
  friendId: number
  friendMemberId: number
  friendName: string
  friendNickname: string
  friendAvatarUrl: string
  friendBannerUrl: string
  friendIntroduce: string
  friendEmail: string
}

export interface DeclineFriendRequestSchema {
  friendId: number
}

export interface DeclineFriendResponseSchema {
  id: number
  friendStatus: FriendStatus
}

export interface AcceptFriendRequestSchema {
  friendId: number
}

export interface AcceptFriendResponseSchema {
  id: number
  friendStatus: FriendStatus
}

export interface CreateRequestForFriendRequestSchema {
  toMemberId: number
}

export interface CreateResponseForFriendResponseSchema {
  friendId: number
  fromMemberId: number
  toMemeberId: number
  createdAt: number
}
