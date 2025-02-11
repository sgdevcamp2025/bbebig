import { CustomPresenceStatus } from './user'

export interface Friend {
  id: number
  avatarUrl: string
  name: string
  status: CustomPresenceStatus
  friendStatus?: FriendStatus
}

type FriendStatus = 'RESPONSE_PENDING' | 'REQUEST_PENDING' | 'DECLINED' | 'ACCEPTED' | 'DELETED'
